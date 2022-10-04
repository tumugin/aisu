package com.tumugin.aisu.app.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

class AisuHTTPClient(engine: HttpClientEngine = CIO.create()) {
  val httpClient = HttpClient(engine) {
    install(ContentNegotiation) {
      json()
    }
  }
}
