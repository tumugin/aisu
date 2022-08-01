package com.tumugin.aisu.testing

import com.expediagroup.graphql.client.ktor.GraphQLKtorClient
import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.createKtorModule
import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.inject
import java.net.URL

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

  suspend fun loginAndGetCookieValue(builder: ApplicationTestBuilder, email: String, password: String): String {
    builder.client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(Json.encodeToString(LoginRequest(email, password)))
      addCSRFTokenHeader(this)
    }.apply {
      return headers.getAll("Set-Cookie")!!.map { parseServerSetCookieHeader(it) }
        .map { (it.name).encodeURLParameter() + "=" + (it.value).encodeURLParameter() }.joinToString("; ")
    }
  }

  suspend fun seedUserAndLoginAndGetCookieValue(builder: ApplicationTestBuilder): LoggedUserAndCookieValue {
    val user = UserSeeder().seedNonDuplicateUser(userRawPassword = UserRawPassword("password"))
    return LoggedUserAndCookieValue(user, loginAndGetCookieValue(builder, user.userEmail!!.value, "password"))
  }

  fun createGraphQLKtorClient(client: HttpClient): GraphQLKtorClient {
    return GraphQLKtorClient(URL("http://localhost/graphql"), client)
  }
}

data class LoggedUserAndCookieValue(val user: User, val cookieValue: String) {
}
