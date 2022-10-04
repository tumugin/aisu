package com.tumugin.aisu.infra.auth0

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.auth0.Auth0UserId
import com.tumugin.aisu.domain.auth0.Auth0UserInfo
import com.tumugin.aisu.domain.auth0.Auth0UserInfoRepository
import com.tumugin.aisu.domain.auth0.Auth0UserName
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Auth0UserInfoRepositoryImpl : Auth0UserInfoRepository, KoinComponent {
  private val aisuHTTPClient by inject<AisuHTTPClient>()
  private val appConfigRepository by inject<AppConfigRepository>()

  override suspend fun getAuth0UserInfoByToken(token: String): Auth0UserInfo {
    val userInfo: UserInfo =
      aisuHTTPClient.httpClient.get("https://${appConfigRepository.appConfig.appConfigAuth0Domain.value}/userinfo") {
        headers {
          append(HttpHeaders.Authorization, "Bearer $token")
        }
      }.body()
    return Auth0UserInfo(
      Auth0UserId(userInfo.userId), Auth0UserName(userInfo.name)
    )
  }

  @Serializable
  private data class UserInfo(
    @SerialName("user_id") val userId: String,
    val name: String,
  )
}
