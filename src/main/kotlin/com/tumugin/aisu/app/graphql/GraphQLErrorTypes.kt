package com.tumugin.aisu.app.graphql

import graphql.ErrorClassification
import io.ktor.server.plugins.*

enum class GraphQLErrorTypes(v: String) : ErrorClassification {
  BadRequest("BAD_REQUEST"),
  UnknownServerError("UNKNOWN_SERVER_ERROR");

  companion object {
    fun fromException(exception: Throwable): GraphQLErrorTypes {
      return when (exception) {
        is BadRequestException -> BadRequest
        else -> UnknownServerError
      }
    }

    fun isSupportedType(exception: Throwable): Boolean {
      return this.fromException(exception) != UnknownServerError
    }
  }
}
