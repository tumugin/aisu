package com.tumugin.aisu.app.graphql

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.LoginFailedException
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.user.UserAlreadyExistException
import graphql.ErrorClassification
import io.ktor.server.plugins.*
import java.lang.reflect.InvocationTargetException

enum class GraphQLErrorTypes(v: String) : ErrorClassification {
  BadRequest("BAD_REQUEST"),
  NotFound("NOT_FOUND"),
  HasNoPermission("HAS_NO_PERMISSION"),
  LoginFailed("LOGIN_FAILED"),
  UserAlreadyExists("USER_ALREADY_EXISTS"),
  NotAuthorized("NOT_AUTHORIZED"),
  UnknownServerError("UNKNOWN_SERVER_ERROR");

  companion object {
    fun fromException(exception: Throwable): GraphQLErrorTypes {
      return when (exception) {
        is InvocationTargetException -> fromException(exception.targetException)
        is BadRequestException -> BadRequest
        is NotFoundException -> NotFound
        is com.tumugin.aisu.domain.exception.NotFoundException -> NotFound
        is HasNoPermissionException -> HasNoPermission
        is LoginFailedException -> LoginFailed
        is UserAlreadyExistException -> UserAlreadyExists
        is NotAuthorizedException -> NotAuthorized
        else -> UnknownServerError
      }
    }

    fun isSupportedType(exception: Throwable): Boolean {
      return this.fromException(exception) != UnknownServerError
    }
  }
}
