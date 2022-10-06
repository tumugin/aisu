package com.tumugin.aisu.app.client

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class AisuHTTPClient(engine: HttpClientEngine = CIO.create()) {
  val httpClient = HttpClient(engine) {
    install(ContentNegotiation) {
      json(Json {
        encodeDefaults = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        allowStructuredMapKeys = true
        prettyPrint = false
        useArrayPolymorphism = false
        ignoreUnknownKeys = true
      })
    }
  }
}
