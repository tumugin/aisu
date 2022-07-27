package com.tumugin.aisu.app.controller

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.tumugin.aisu.app.graphql.KtorGraphQLServer.Companion.getGraphQLServer
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class GraphQLServerController {
  private val mapper = jacksonObjectMapper()

  suspend fun handle(applicationCall: ApplicationCall) {
    val ktorGraphQLServer = getGraphQLServer(mapper)
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
