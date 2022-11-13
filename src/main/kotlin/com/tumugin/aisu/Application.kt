package com.tumugin.aisu

import com.tumugin.aisu.app.plugins.*
import com.tumugin.aisu.di.AisuDIModule
import com.tumugin.aisu.usecase.app.ApplicationBootstrap
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.Koin
import org.koin.core.context.GlobalContext

fun main() {
  AisuDIModule.start()
  ApplicationBootstrap().bootstrap()
  embeddedServer(
    Netty,
    port = 8080,
    host = "0.0.0.0",
    module = createKtorModule(GlobalContext.get())
  ).start(wait = true)
}

fun createKtorModule(koin: Koin): Application.() -> Unit {
  val ktorModule: Application.() -> Unit = {
    configureRouting(koin)
    configureSerialization()
    configureHTTP(koin)
    configureSecurity(koin)
    configureAuthentication(koin)
    configureSentry(koin)
  }
  return ktorModule
}
