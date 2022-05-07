package com.tumugin.aisu

import com.tumugin.aisu.app.plugins.*
import com.tumugin.aisu.di.AisuDIModule
import com.tumugin.aisu.usecase.app.ApplicationBootstrap
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
  AisuDIModule.start()
  ApplicationBootstrap.bootstrap()
  embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureAuthentication()
  }.start(wait = true)
}
