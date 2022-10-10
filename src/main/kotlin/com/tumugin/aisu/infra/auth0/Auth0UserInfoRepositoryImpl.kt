package com.tumugin.aisu.infra.auth0

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.auth0.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Auth0UserInfoRepositoryImpl : Auth0UserInfoRepository, KoinComponent {
  private val aisuHTTPClient by inject<AisuHTTPClient>()
  private val appConfigRepository by inject<AppConfigRepository>()

  override suspend fun getAuth0UserInfoByToken(token: Auth0Token): Auth0UserInfo {
    val userInfo: UserInfo =
      aisuHTTPClient.httpClient.get("https://${appConfigRepository.appConfig.appConfigAuth0Domain.value}/userinfo") {
        headers {
          append(HttpHeaders.Authorization, "Bearer ${token.value}")
        }
      }.body()
    return Auth0UserInfo(
      Auth0UserId(userInfo.sub), Auth0UserName(userInfo.name)
    )
  }

  @Serializable
  private data class UserInfo(
    val sub: String,
    val name: String,
  )
}
