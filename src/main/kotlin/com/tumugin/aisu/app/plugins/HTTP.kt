package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.doublereceive.*
import io.ktor.server.plugins.forwardedheaders.*
import org.koin.core.Koin
import java.net.URI

fun Application.configureHTTP(koin: Koin) {
  val appConfigRepository = koin.get<AppConfigRepository>()

  install(DoubleReceive)
  install(DefaultHeaders) {
    header("X-Engine", "Ktor") // will send this header with each response
  }
  install(ForwardedHeaders)
  install(XForwardedHeaders)
  install(CORS) {
    // method
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Get)
    allowMethod(HttpMethod.Post)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowMethod(HttpMethod.Head)
    // credentials
    allowCredentials = true
    // header
    allowHeaders {
      true
    }
    // content type
    allowNonSimpleContentTypes = true
    // host
    allowHost(URI(appConfigRepository.appConfig.appConfigAppUrl.value).host, listOf("http", "https"))
    allowHost(URI(appConfigRepository.appConfig.appConfigAdminAppUrl.value).host, listOf("http", "https"))
    appConfigRepository.appConfig.appConfigCORSAllowHosts.value.forEach {
      allowHost(it, listOf("http", "https"))
    }
  }
}
