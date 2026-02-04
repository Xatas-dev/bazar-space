package org.bazar.space.config.authorization

import io.github.oshai.kotlinlogging.KotlinLogging
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.bazar.space.config.authorization.client.AuthorizationGrpcClient
import org.bazar.space.util.exceptions.ApiException
import org.bazar.space.util.exceptions.ApiExceptions
import org.bazar.space.util.exceptions.ApiExceptions.AUTHORIZATION_SERVICE_ERROR
import org.bazar.space.util.exceptions.ApiExceptions.FORBIDDEN
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.PathVariable

@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class SpacePreAuthorizeAOPHandler(
    private val authzClient: AuthorizationGrpcClient
) {

    private val logger = KotlinLogging.logger { }

    @Around("@annotation(SpacePreAuthorize)")
    fun authorizeSpaceAccess(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val annotation = method.getAnnotation(SpacePreAuthorize::class.java)

        val context = SecurityContextHolder.getContext().authentication?.principal as? Jwt ?: throw ApiException(
            ApiExceptions.NOT_AUTHENTICATED
        )

        val spaceId = resolveSpaceIdEnhanced(joinPoint, annotation)

        val isAuthorized = try {
            authzClient.checkAccess(spaceId, annotation.action)
        } catch (ex: Exception) {
            logger.error(ex) { "Authorization service call failed for spaceId: $spaceId" }
            throw ApiException(AUTHORIZATION_SERVICE_ERROR)
        }

        if (!isAuthorized) {
            logger.warn { "Authorization call returned DENY on action ${annotation.action} for user=${context.subject} and space=$spaceId" }
            throw ApiException(FORBIDDEN)
        }

        logger.debug { "Space Authorization: access decision from bazar-authorization TRUE for spaceId=$spaceId" }
        return joinPoint.proceed()
    }

    private fun resolveSpaceIdEnhanced(
        joinPoint: ProceedingJoinPoint,
        annotation: SpacePreAuthorize
    ): Long {
        // Priority 1: Explicit parameter name from annotation
        if (annotation.spaceIdParam.isNotBlank()) {
            return extractNamedParameter(joinPoint, annotation.spaceIdParam)
        }

        // Priority 2: Field from request body
        if (annotation.spaceIdField.isNotBlank()) {
            return extractFieldFromRequestBody(joinPoint, annotation.spaceIdField)
        }

        // Priority 3: Auto-detection
        return autoDetectSpaceId(joinPoint)
    }

    private fun extractNamedParameter(joinPoint: ProceedingJoinPoint, paramName: String): Long {
        val methodSignature = joinPoint.signature as MethodSignature
        val parameterNames = methodSignature.parameterNames
        val args = joinPoint.args

        for (i in parameterNames.indices) {
            if (parameterNames[i] == paramName) {
                return convertToLong(args[i])
            }
        }
        throw IllegalArgumentException("Parameter '$paramName' not found in method")
    }

    private fun extractFieldFromRequestBody(joinPoint: ProceedingJoinPoint, fieldPath: String): Long {
        val methodSignature = joinPoint.signature as MethodSignature
        val parameterAnnotations = methodSignature.method.parameterAnnotations
        val args = joinPoint.args

        for (i in parameterAnnotations.indices) {
            for (annotation in parameterAnnotations[i]) {
                if (annotation is org.springframework.web.bind.annotation.RequestBody) {
                    val requestBody = args[i]
                    return extractNestedField(requestBody, fieldPath)
                }
            }
        }
        throw IllegalArgumentException("@RequestBody not found for field extraction")
    }

    private fun autoDetectSpaceId(joinPoint: ProceedingJoinPoint): Long {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        val parameterAnnotations = method.parameterAnnotations
        val args = joinPoint.args

        // Check for @PathVariable with name containing "space"
        for (i in parameterAnnotations.indices) {
            for (annotation in parameterAnnotations[i]) {
                if (annotation is PathVariable) {
                    val name = annotation.name.ifBlank { annotation.value }
                    if (name.contains("space", ignoreCase = true)) {
                        return convertToLong(args[i])
                    }
                }
            }
        }

        // Check for any @PathVariable (fallback)
        for (i in parameterAnnotations.indices) {
            for (annotation in parameterAnnotations[i]) {
                if (annotation is PathVariable) {
                    return convertToLong(args[i])
                }
            }
        }

        // Check for spaceId/id field in @RequestBody
        for (i in parameterAnnotations.indices) {
            for (annotation in parameterAnnotations[i]) {
                if (annotation is org.springframework.web.bind.annotation.RequestBody) {
                    val requestBody = args[i]
                    return try {
                        extractNestedField(requestBody, "spaceId")
                    } catch (ex: Exception) {
                        try {
                            extractNestedField(requestBody, "id")
                        } catch (ex2: Exception) {
                            throw IllegalArgumentException("No spaceId or id field found in request body")
                        }
                    }
                }
            }
        }

        throw IllegalArgumentException("Could not auto-detect spaceId from method parameters")
    }

    private fun extractNestedField(obj: Any, fieldPath: String): Long {
        var current: Any = obj
        val fields = fieldPath.split(".")

        for (field in fields) {
            val fieldObj = current.javaClass.getDeclaredField(field).apply {
                isAccessible = true
            }
            current = fieldObj.get(current)
        }

        return convertToLong(current)
    }

    private fun convertToLong(value: Any?): Long {
        return when (value) {
            null -> throw IllegalArgumentException("Value is null")
            is Long -> value
            is Number -> value.toLong()
            is String -> value.toLongOrNull() ?: throw IllegalArgumentException("Invalid number format: $value")
            else -> throw IllegalArgumentException("Cannot convert ${value::class.simpleName} to Long")
        }
    }
}