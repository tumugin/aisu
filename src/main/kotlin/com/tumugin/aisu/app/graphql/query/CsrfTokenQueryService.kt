package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CsrfTokenQueryService : Query, KoinComponent {
  private val csrfRepository by inject<CSRFRepository>()

  suspend fun getCsrfToken(): String {
    return csrfRepository.generateToken().value
  }
}
