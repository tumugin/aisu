package com.tumugin.aisu

import com.tumugin.aisu.app.plugins.*
import com.tumugin.aisu.di.AisuDIModule
import com.tumugin.aisu.usecase.app.ApplicationBootstrap
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.logging.*
import org.koin.core.Koin
import org.koin.core.context.GlobalContext
import org.slf4j.LoggerFactory

suspend fun main() {
  val logger = LoggerFactory.getLogger("Startup")
  try {
    AisuDIModule.start()
    ApplicationBootstrap().bootstrap()
    embeddedServer(
      Netty,
      port = 8080,
      host = "0.0.0.0",
      module = createKtorModule(GlobalContext.get())
    ).start(wait = true)
  } catch (e: Exception) {
    logger.error(e)
    throw e
  }
}

fun createKtorModule(koin: Koin): Application.() -> Unit {
  val ktorModule: Application.() -> Unit = {
    configureAuthentication(koin)
    configureRouting(koin)
    configureSerialization()
    configureHTTP(koin)
    configureSecurity(koin)
    configureRefreshToken()
    configureAdminRefreshToken()
    configureSentry(koin)
  }
  return ktorModule
}
