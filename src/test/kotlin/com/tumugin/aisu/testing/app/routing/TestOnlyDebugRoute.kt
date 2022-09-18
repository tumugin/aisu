package com.tumugin.aisu.testing.app.routing

import com.tumugin.aisu.domain.app.config.AppConfig
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.config.AppEnvironment
import com.tumugin.aisu.infra.app.config.AppConfigRepositoryImpl
import com.tumugin.aisu.testing.BaseKtorTest
import io.ktor.client.request.*
import io.ktor.http.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.test.mock.declare

class TestOnlyDebugRoute : BaseKtorTest() {
  @Test
  fun testProductionMode() {
    val originalRepository = AppConfigRepositoryImpl(isTesting = true)
    val mockedRepository = mockk<AppConfigRepository>()
    every { mockedRepository.appConfig } returns AppConfig(
      originalRepository.appConfig.appConfigDatabaseJdbcUrl,
      originalRepository.appConfig.appConfigDatabaseUserName,
      originalRepository.appConfig.appConfigDatabasePassword,
      originalRepository.appConfig.appConfigRedisHost,
      originalRepository.appConfig.appConfigRedisPort,
      AppEnvironment.PRODUCTION
    )
    declare {
      mockedRepository
    }
    testAisuApplication {
      client.get("/sdl").apply {
        Assertions.assertEquals(HttpStatusCode.NotFound, status)
      }
    }
  }

  @Test
  fun testTestingMode() {
    testAisuApplication {
      client.get("/sdl").apply {
        Assertions.assertEquals(HttpStatusCode.OK, status)
      }
    }
  }
}