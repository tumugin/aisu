package com.tumugin.aisu.testing

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.tumugin.aisu.app.plugins.AdminUserAuthSession
import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.createKtorModule
import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import org.koin.core.component.inject
import java.net.URL
import kotlin.time.Duration.Companion.days

abstract class BaseKtorTest : BaseDatabaseTest() {
  private val csrfRepository by inject<CSRFRepository>()

  fun <T> testAisuApplication(block: suspend ApplicationTestBuilder.() -> T): T {
    var result: T? = null
    testApplication {
      application(createKtorModule(getKoin()))
      result = block()
    }
    return result!!
  }

  fun addCSRFTokenHeader(builder: HttpRequestBuilder) {
    builder.header("X-CSRF-Token", csrfRepository.generateToken().value)
  }

  suspend fun loginAndGetCookieValue(builder: ApplicationTestBuilder, user: User): String {
    builder.routing {
      get("/test-add-session") {
        call.sessions.set(
          UserAuthSession(
            userId = user.userId.value,
            validThroughTimestamp = Clock.System.now().plus(30.days).toString(),
            forceLogoutGeneration = user.userForceLogoutGeneration.value
          )
        )
      }
    }
    builder.client.get("/test-add-session").apply {
      return headers.getAll("Set-Cookie")!!.map { parseServerSetCookieHeader(it) }
        .map { (it.name).encodeURLParameter() + "=" + (it.value).encodeURLParameter() }.joinToString("; ")
    }
  }

  suspend fun adminLoginAndGetCookieValue(builder: ApplicationTestBuilder, adminUser: AdminUser): String {
    builder.routing {
      get("/test-add-admin-session") {
        call.sessions.set(
          AdminUserAuthSession(
            adminUserId = adminUser.adminUserId.value,
            validThroughTimestamp = Clock.System.now().plus(30.days).toString(),
            forceLogoutGeneration = adminUser.adminUserForceLogoutGeneration.value
          )
        )
      }
    }
    builder.client.get("/test-add-admin-session").apply {
      return headers.getAll("Set-Cookie")!!.map { parseServerSetCookieHeader(it) }
        .map { (it.name).encodeURLParameter() + "=" + (it.value).encodeURLParameter() }.joinToString("; ")
    }
  }

  suspend fun seedUserAndLoginAndGetCookieValue(builder: ApplicationTestBuilder): LoggedUserAndCookieValue {
    val user = UserSeeder().seedNonDuplicateUser(userRawPassword = UserRawPassword("password"))
    return LoggedUserAndCookieValue(user, loginAndGetCookieValue(builder, user))
  }

  fun createGraphQLKtorClient(client: HttpClient): GraphQLKtorClient {
    return GraphQLKtorClient(URL("http://localhost/graphql"), client)
  }
}

data class LoggedUserAndCookieValue(val user: User, val cookieValue: String) {}
