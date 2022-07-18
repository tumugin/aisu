package com.tumugin.aisu.app.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.doublereceive.*

fun Application.configureHTTP() {
  install(DoubleReceive)
  install(DefaultHeaders) {
    header("X-Engine", "Ktor") // will send this header with each response
  }
  install(CORS) {
    allowMethod(HttpMethod.Options)
    allowMethod(HttpMethod.Put)
    allowMethod(HttpMethod.Delete)
    allowMethod(HttpMethod.Patch)
    allowHeader(HttpHeaders.Authorization)
    anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
  }
}
