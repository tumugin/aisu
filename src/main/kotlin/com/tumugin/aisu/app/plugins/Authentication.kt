package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.client.AisuHTTPClient
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.domain.user.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.core.Koin
import java.net.URL

fun Application.configureAuthentication(koin: Koin) {
  val userRepository = koin.get<UserRepository>()
  val adminUserRepository = koin.get<AdminUserRepository>()
  val aisuHTTPClient = koin.get<AisuHTTPClient>()
  val appConfigRepository = koin.get<AppConfigRepository>()
  val appConfig = appConfigRepository.appConfig

  install(Authentication) {
    session<UserAuthSession>("user_session") {
      challenge {
        call.respond(HttpStatusCode.Unauthorized)
      }
      validate { session ->
        val user = userRepository.getUserById(UserId(session.userId))
        return@validate if (user?.canLoginFromSessionParams(
            session.validThroughTimestamp, session.forceLogoutGeneration
          ) == false
        ) {
          // セッションからのログイン不許可の場合
          null
        } else if (user != null) {
          // 許可の場合
          UserPrincipal(user)
        } else {
          // ユーザが存在しない場合
          null
        }
      }
    }
    session<AdminUserAuthSession>("admin_user_session") {
      challenge {
        call.respond(HttpStatusCode.Unauthorized)
      }
      validate { session ->
        val adminUser = adminUserRepository.getAdminUserById(AdminUserId(session.adminUserId))
        return@validate if (adminUser?.canLoginFromSessionParams(
            session.validThroughTimestamp, session.forceLogoutGeneration
          ) == false
        ) {
          // セッションからのログイン不許可の場合
          null
        } else if (adminUser != null) {
          // 許可の場合
          AdminUserPrincipal(adminUser)
        } else {
          // ユーザが存在しない場合
          null
        }
      }
    }
    oauth("auth-oauth-auth0") {
      urlProvider = { URL(URL(appConfig.appConfigAppUrl.value), "/auth0/callback").toString() }
      providerLookup = {
        OAuthServerSettings.OAuth2ServerSettings(
          name = "auth0",
          authorizeUrl = "https://${appConfig.appConfigAuth0Domain.value}/authorize",
          accessTokenUrl = "https://${appConfig.appConfigAuth0Domain.value}/oauth/token",
          requestMethod = HttpMethod.Post,
          clientId = appConfig.appConfigAuth0ClientId.value,
          clientSecret = appConfig.appConfigAuth0ClientSecret.value,
          defaultScopes = listOf("openid", "profile", "email"),
        )
      }
      client = aisuHTTPClient.httpClient
    }
    oauth("admin-auth-oauth-auth0") {
      urlProvider = { URL(URL(appConfig.appConfigAdminAppUrl.value), "/admin/auth0/callback").toString() }
      providerLookup = {
        OAuthServerSettings.OAuth2ServerSettings(
          name = "admin-auth0",
          authorizeUrl = "https://${appConfig.appConfigAdminAuth0Domain.value}/authorize",
          accessTokenUrl = "https://${appConfig.appConfigAdminAuth0Domain.value}/oauth/token",
          requestMethod = HttpMethod.Post,
          clientId = appConfig.appConfigAdminAuth0ClientId.value,
          clientSecret = appConfig.appConfigAdminAuth0ClientSecret.value,
          defaultScopes = listOf("openid", "profile", "email"),
        )
      }
      client = aisuHTTPClient.httpClient
    }
  }
}
