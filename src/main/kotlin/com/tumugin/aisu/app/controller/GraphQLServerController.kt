package com.tumugin.aisu.app.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.tumugin.aisu.app.graphql.KtorGraphQLServer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GraphQLServerController : KoinComponent {
  private val mapper by inject<ObjectMapper>()
  private val ktorGraphQLServer by inject<KtorGraphQLServer>()

  suspend fun handle(applicationCall: ApplicationCall) {
    val result = ktorGraphQLServer.execute(applicationCall.request)

    if (result != null) {
      // write response as json
      val json = mapper.writeValueAsString(result)
      applicationCall.response.call.respondText(json, ContentType.Application.Json)
    } else {
      applicationCall.response.call.respond(HttpStatusCode.BadRequest, "Invalid request")
    }
  }
}
