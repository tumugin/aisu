package com.tumugin.aisu.usecase.client.auth0

import com.tumugin.aisu.domain.auth0.Auth0Token
import com.tumugin.aisu.domain.auth0.Auth0User
import com.tumugin.aisu.domain.auth0.Auth0UserInfoRepository
import com.tumugin.aisu.domain.auth0.Auth0UserRepository
import io.ktor.server.auth.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginOrCreateUserByAuth0Callback : KoinComponent {
  private val auth0UserInfoRepository by inject<Auth0UserInfoRepository>()
  private val auth0UserRepository by inject<Auth0UserRepository>()

  suspend fun getOrCreateUserByPrincipal(principal: OAuthAccessTokenResponse.OAuth2): Auth0User {
    val auth0UserInfo = auth0UserInfoRepository.getAuth0UserInfoByToken(
      Auth0Token(principal.accessToken)
    )
    return auth0UserRepository.getAuth0User(auth0UserInfo) ?: auth0UserRepository.addAuth0User(auth0UserInfo)
  }
}
