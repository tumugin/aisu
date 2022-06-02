package com.tumugin.aisu.testing

import com.tumugin.aisu.createKtorModule
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest : BaseDatabaseTest() {
  @Test
  fun testRoot() = testApplication {
    application(createKtorModule(getKoin()))
    client.get("/").apply {
      assertEquals(HttpStatusCode.OK, status)
      assertEquals("Hello World!", bodyAsText())
    }
  }
}
