package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.config.AppEnvironment
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.doublereceive.*
import org.koin.core.Koin

fun Application.configureHTTP(koin: Koin) {
  val appConfigRepository = koin.get<AppConfigRepository>()

  install(DoubleReceive)
  install(DefaultHeaders) {
    header("X-Engine", "Ktor") // will send this header with each response
  }
  install(CORS) {
    // method
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowMethod(HttpMethod.Head)
    // header
    allowHeaders {
      true
    }
    // content type
    allowNonSimpleContentTypes = true
    // host
    if (appConfigRepository.appConfig.appEnvironment === AppEnvironment.LOCAL) {
      anyHost()
    }
  }
}
