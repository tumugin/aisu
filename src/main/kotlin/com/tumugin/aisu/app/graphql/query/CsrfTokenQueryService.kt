package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.usecase.app.CsrfToken

class CsrfTokenQueryService : Query {
  private val csrfToken = CsrfToken()

  suspend fun getCsrfToken(): String {
    return csrfToken.getCsrfToken().value
  }
}
