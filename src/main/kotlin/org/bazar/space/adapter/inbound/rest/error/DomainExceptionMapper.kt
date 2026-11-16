package org.bazar.space.adapter.inbound.rest.error

import org.bazar.space.domain.exception.DomainException
import org.bazar.space.domain.exception.ForbiddenException
import org.bazar.space.domain.exception.InvalidAvatarFileExtensionException
import org.bazar.space.domain.exception.InvalidAvatarFileTypeException
import org.bazar.space.domain.exception.InvalidAvatarSizeException
import org.bazar.space.domain.exception.NotAuthenticatedException
import org.bazar.space.domain.exception.SpaceAvatarNotFoundException
import org.bazar.space.domain.exception.SpaceNotFoundException
import org.bazar.space.domain.exception.UserNotFoundException
import org.springframework.http.HttpStatus

fun DomainException.toHttpError(): HttpError = when (this) {
    is ForbiddenException ->
        httpError(HttpStatus.FORBIDDEN, "FORBIDDEN", "Недостаточно прав для выполнения действия")

    is NotAuthenticatedException ->
        httpError(HttpStatus.UNAUTHORIZED, "NOT_AUTHENTICATED", "Пользователь не вошел в акк")

    is SpaceNotFoundException ->
        httpError(HttpStatus.NOT_FOUND, "SPACE_NOT_FOUND", "Спейс не найден")

    is UserNotFoundException ->
        httpError(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "Пользователь не найден")

    is SpaceAvatarNotFoundException ->
        httpError(HttpStatus.NOT_FOUND, "SPACE_AVATAR_NOT_FOUND", "Ошибка при обработке аватарки, попробуйте позже")

    is InvalidAvatarFileExtensionException ->
        httpError(HttpStatus.BAD_REQUEST, "INVALID_AVATAR_FILE_EXTENSION", "Неподдерживаемое расширение файла $fileExtension")

    is InvalidAvatarFileTypeException ->
        httpError(HttpStatus.BAD_REQUEST, "INVALID_AVATAR_FILE_TYPE", "Неподдерживаемый тип файла $fileType")

    is InvalidAvatarSizeException ->
        httpError(HttpStatus.BAD_REQUEST, "INVALID_AVATAR_SIZE", "Размер файла слишком большой")
}

private fun httpError(status: HttpStatus, code: String, message: String): HttpError =
    HttpError(
        status = status,
        body = ApiErrorResponse(
            code = code,
            message = message
        )
    )