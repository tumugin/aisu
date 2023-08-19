package com.tumugin.aisu.usecase.client.auth0

import com.tumugin.aisu.domain.auth0.Auth0LogoutUrl
import com.tumugin.aisu.domain.auth0.Auth0Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAuth0LogoutUrl : KoinComponent {
  private val auth0Repository by inject<Auth0Repository>()

  fun getAuth0LogoutUrl(): Auth0LogoutUrl {
    return auth0Repository.generateAuth0LogoutUrl()
  }
}
