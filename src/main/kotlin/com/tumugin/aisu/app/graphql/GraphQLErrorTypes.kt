package com.tumugin.aisu.app.graphql

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.LoginFailedException
import graphql.ErrorClassification
import io.ktor.server.plugins.*

enum class GraphQLErrorTypes(v: String) : ErrorClassification {
  BadRequest("BAD_REQUEST"),
  NotFound("NOT_FOUND"),
  HasNoPermission("HAS_NO_PERMISSION"),
  LoginFailed("LOGIN_FAILED"),
  UnknownServerError("UNKNOWN_SERVER_ERROR");

  companion object {
    fun fromException(exception: Throwable): GraphQLErrorTypes {
      return when (exception) {
        is BadRequestException -> BadRequest
        is NotFoundException -> NotFound
        is com.tumugin.aisu.domain.exception.NotFoundException -> NotFound
        is HasNoPermissionException -> HasNoPermission
        is LoginFailedException -> LoginFailed
        else -> UnknownServerError
      }
    }

    fun isSupportedType(exception: Throwable): Boolean {
      return this.fromException(exception) != UnknownServerError
    }
  }
}
