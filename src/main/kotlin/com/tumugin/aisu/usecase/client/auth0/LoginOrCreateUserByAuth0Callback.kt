package com.tumugin.aisu.usecase.client.auth0

import com.tumugin.aisu.domain.auth0.Auth0Repository
import com.tumugin.aisu.domain.user.User
import io.ktor.server.auth.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginOrCreateUserByAuth0Callback : KoinComponent {
  private val auth0Repository by inject<Auth0Repository>()

  suspend fun getOrCreateUserByPrincipal(principal: OAuthAccessTokenResponse.OAuth2): User {
    val auth0UserInfo = auth0Repository.getAuth0UserInfoByToken(principal.accessToken)
    TODO("not implemented")
  }
}
