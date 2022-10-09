package com.tumugin.aisu.infra.auth0

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.adminAuth0.*
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdminAuth0RepositoryImpl : AdminAuth0Repository, KoinComponent {
  private val aisuHTTPClient by inject<AisuHTTPClient>()
  private val appConfigRepository by inject<AppConfigRepository>()

  override suspend fun getAuth0UserInfoByToken(token: AdminAuth0Token): AdminAuth0UserInfo {
    val userInfo: UserInfo =
      aisuHTTPClient.httpClient.get("https://${appConfigRepository.appConfig.appConfigAuth0Domain.value}/userinfo") {
        headers {
          append(HttpHeaders.Authorization, "Bearer $token")
        }
      }.body()
    return AdminAuth0UserInfo(
      AdminAuth0Email(userInfo.email), AdminAuth0UserId(userInfo.sub), AdminAuth0UserName(userInfo.name)
    )
  }

  @Serializable
  private data class UserInfo(
    val sub: String, val name: String, val email: String
  )
}
