package com.tumugin.aisu.testing.app.controller.api.user.chekis

import com.tumugin.aisu.testing.BaseKtorTest
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class UserChekisIdolCountControllerTest : BaseKtorTest() {
  @Test
  fun index() = testAisuApplication {
    client.get("/api/user/chekis/idol_count") {
      contentType(ContentType.Application.Json)
      header("Cookie", seedUserAndLoginAndGetCookieValue(this@testAisuApplication).cookieValue)
      parameter("chekiShotAtStart", "2022-01-01T00:00:00+09:00")
      parameter("chekiShotAtEnd", "2022-01-31T23:59:59+09:00")
    }.apply {
      Assertions.assertEquals(HttpStatusCode.OK, status)
    }
  }
}
