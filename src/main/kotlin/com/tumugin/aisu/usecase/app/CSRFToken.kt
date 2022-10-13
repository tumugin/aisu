package com.tumugin.aisu.usecase.app

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CSRFToken : KoinComponent {
  private val csrfRepository by inject<CSRFRepository>()

  suspend fun getCsrfToken(): CSRFToken {
    return csrfRepository.generateToken()
  }
}
