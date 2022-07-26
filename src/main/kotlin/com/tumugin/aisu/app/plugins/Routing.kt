package com.tumugin.aisu.app.plugins

import com.expediagroup.graphql.generator.extensions.print
import com.tumugin.aisu.app.controller.GraphQLServerController
import com.tumugin.aisu.app.controller.api.ChekisController
import com.tumugin.aisu.app.controller.api.LoginController
import com.tumugin.aisu.app.controller.api.LogoutController
import com.tumugin.aisu.app.controller.api.MetadataController
import com.tumugin.aisu.app.controller.api.user.UserChekisController
import com.tumugin.aisu.app.controller.api.user.chekis.UserChekisIdolCountController
import com.tumugin.aisu.app.graphql.GraphQLSchema
import com.tumugin.aisu.app.plugins.security.CsrfProtection
import com.tumugin.aisu.domain.cheki.ChekiId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
  install(Locations) {
  }

  install(CsrfProtection) {
    applyToAllRoutesWithSideEffects()
  }

  routing {
    route("api") {
      authenticate("user_session") {
        route("user") {
          route("chekis") {
            get {
              UserChekisController().get(call)
            }
            get("idol_count") {
              UserChekisIdolCountController().get(call)
            }
          }
        }
        route("chekis") {
          get<ResourceIdGetRequest> {
            ChekisController().get(call, ChekiId(it.id))
          }
        }
      }
      post("login") {
        LoginController().post(call)
      }
      post("logout") {
        LogoutController().post(call)
      }
      get("metadata") {
        MetadataController().get(call)
      }
    }
    get("/") {
      call.respondText("aisu")
    }
    // graphql
    post("graphql") {
      GraphQLServerController().handle(this.call)
    }
    get("sdl") {
      call.respondText(GraphQLSchema().graphQLSchema.print())
    }
    get("playground") {
      this.call.respondText(
        buildPlaygroundHtml("graphql", "subscriptions"),
        ContentType.Text.Html
      )
    }
  }
}

@Location("{id}")
class ResourceIdGetRequest(val id: Long)

private fun buildPlaygroundHtml(graphQLEndpoint: String, subscriptionsEndpoint: String) =
  Application::class.java.classLoader.getResource("graphql-playground.html")?.readText()
    ?.replace("\${graphQLEndpoint}", graphQLEndpoint)
    ?.replace("\${subscriptionsEndpoint}", subscriptionsEndpoint)
    ?: throw IllegalStateException("graphql-playground.html cannot be found in the classpath")
