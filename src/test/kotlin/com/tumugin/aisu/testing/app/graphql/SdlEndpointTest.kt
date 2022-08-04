package com.tumugin.aisu.testing.app.graphql

import com.tumugin.aisu.testing.BaseKtorTest
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class SdlEndpointTest : BaseKtorTest() {
  @Test
  fun testSdlEndpoint(): Unit = testAisuApplication {
    client.get("/sdl").apply {
      Assertions.assertEquals(HttpStatusCode.OK, status)
    }
  }
}
