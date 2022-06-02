package com.tumugin.aisu

import io.ktor.server.testing.*

abstract class BaseKtorTest : BaseDatabaseTest() {
  fun testAisuApplication(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
      application(createKtorModule(getKoin()))
      block()
    }
  }
}
