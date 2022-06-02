package com.tumugin.aisu.testing

import com.tumugin.aisu.createKtorModule
import io.ktor.server.testing.*

abstract class BaseKtorTest : BaseDatabaseTest() {
  fun testAisuApplication(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
      application(createKtorModule(getKoin()))
      block()
    }
  }
}
