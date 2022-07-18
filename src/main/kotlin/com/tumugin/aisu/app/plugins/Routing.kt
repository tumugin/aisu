package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.controller.api.ChekisController
import com.tumugin.aisu.app.controller.api.LoginController
import com.tumugin.aisu.app.controller.api.LogoutController
import com.tumugin.aisu.app.controller.api.MetadataController
import com.tumugin.aisu.app.controller.api.user.UserChekisController
import com.tumugin.aisu.app.controller.api.user.chekis.UserChekisIdolCountController
import com.tumugin.aisu.app.plugins.security.CsrfProtection
import com.tumugin.aisu.domain.cheki.ChekiId
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
  }
}

@Location("{id}")
class ResourceIdGetRequest(val id: Long)
