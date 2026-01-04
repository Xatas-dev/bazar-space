package org.bazar.space.config.authorization

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SpacePreAuthorize(
    val action: AuthorizationAction,
    val spaceIdParam: String = "", // Explicit parameter name
    val spaceIdField: String = "" // Field name in request body
)
