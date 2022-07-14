package com.tumugin.aisu.testing

import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.createKtorModule
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.testing.seeder.UserSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

abstract class BaseKtorTest : BaseDatabaseTest() {
  fun testAisuApplication(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
      application(createKtorModule(getKoin()))
      block()
    }
  }

  suspend fun loginAndGetCookieValue(builder: ApplicationTestBuilder, email: String, password: String): String {
    builder.client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(Json.encodeToString(LoginRequest(email, password)))
    }.apply {
      return headers.getAll("Set-Cookie")!!.map { parseServerSetCookieHeader(it) }
        .map { (it.name).encodeURLParameter() + "=" + (it.value).encodeURLParameter() }.joinToString("; ")
    }
  }

  suspend fun seedUserAndLoginAndGetCookieValue(builder: ApplicationTestBuilder): LoggedUserAndCookieValue {
    val user = UserSeeder().seedNonDuplicateUser(userRawPassword = UserRawPassword("password"))
    return LoggedUserAndCookieValue(user, loginAndGetCookieValue(builder, user.userEmail!!.value, "password"))
  }
}

data class LoggedUserAndCookieValue(val user: User, val cookieValue: String) {
}
