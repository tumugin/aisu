package com.tumugin.aisu.infra.auth0

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.auth0.Auth0LogoutUrl
import com.tumugin.aisu.domain.auth0.Auth0MailAddress
import com.tumugin.aisu.domain.auth0.Auth0Repository
import io.ktor.client.request.*
import io.ktor.http.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URLEncoder

class Auth0RepositoryImpl : Auth0Repository, KoinComponent {
  private val appConfigRepository by inject<AppConfigRepository>()
  private val aisuHTTPClient by inject<AisuHTTPClient>()

  override fun generateAuth0LogoutUrl(): Auth0LogoutUrl {
    return Auth0LogoutUrl(
      "https://${appConfigRepository.appConfig.appConfigAuth0Domain.value}/v2/logout?" +
        "returnTo=${URLEncoder.encode(appConfigRepository.appConfig.appConfigAppUrl.value, "utf-8")}&" +
        "client_id=${appConfigRepository.appConfig.appConfigAuth0ClientId.value}"
    )
  }

  override suspend fun requestPasswordReset(auth0MailAddress: Auth0MailAddress) {
    aisuHTTPClient.httpClient.post("https://${appConfigRepository.appConfig.appConfigAuth0Domain.value}/dbconnections/change_password") {
      contentType(ContentType.Application.Json)
      setBody(
        mapOf(
          "email" to auth0MailAddress.value,
          "client_id" to appConfigRepository.appConfig.appConfigAuth0ClientId.value,
          "connection" to "Username-Password-Authentication"
        )
      )
    }
  }
}
