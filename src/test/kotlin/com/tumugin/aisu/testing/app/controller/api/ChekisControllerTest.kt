package com.tumugin.aisu.testing.app.controller.api

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.seeder.ChekiSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Test

class ChekisControllerTest : BaseKtorTest() {
  @Test
  fun get() = testAisuApplication {
    val userAndCookie = seedUserAndLoginAndGetCookieValue(this@testAisuApplication)
    val cheki =
      ChekiSeeder().seedCheki(
        userAndCookie.user.userId,
        IdolSeeder().seedIdol(userAndCookie.user.userId).idolId,
        null
      )
    client.get("/api/chekis/${cheki.chekiId.value}") {
      contentType(ContentType.Application.Json)
      header("Cookie", userAndCookie.cookieValue)
    }
  }
}
