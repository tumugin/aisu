package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.usecase.app.CSRFToken

class CsrfTokenQueryService : Query {
  private val csrfToken = CSRFToken()

  suspend fun getCsrfToken(): String {
    return csrfToken.getCsrfToken().value
  }
}
