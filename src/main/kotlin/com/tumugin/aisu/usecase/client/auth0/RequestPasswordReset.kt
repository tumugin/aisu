package com.tumugin.aisu.usecase.client.auth0

import com.tumugin.aisu.domain.auth0.Auth0MailAddress
import com.tumugin.aisu.domain.auth0.Auth0Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RequestPasswordReset : KoinComponent {
  private val auth0Repository by inject<Auth0Repository>()

  suspend fun requestPasswordReset(auth0MailAddress: Auth0MailAddress) {
    auth0Repository.requestPasswordReset(auth0MailAddress)
  }
}
