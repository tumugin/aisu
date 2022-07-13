package com.tumugin.aisu.testing.app.controller.api.user

import com.tumugin.aisu.testing.BaseKtorTest
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserChekisControllerTest : BaseKtorTest() {
  @Test
  fun index() = testAisuApplication {
    client.get("/api/user/chekis") {
      contentType(ContentType.Application.Json)
      header("Cookie", seedUserAndLoginAndGetCookieValue(this@testAisuApplication))
    }.apply {
      Assertions.assertEquals(HttpStatusCode.OK, status)
    }
  }
}
