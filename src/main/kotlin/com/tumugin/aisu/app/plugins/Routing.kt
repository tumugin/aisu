package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.controller.api.LoginController
import com.tumugin.aisu.app.controller.api.LogoutController
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
  install(Locations) {
  }

  routing {
    route("/api") {
      post("login") {
        LoginController().post(call)
      }
      post("logout") {
        LogoutController().post(call)
      }
    }
    get("/") {
      call.respondText("Hello World!")
    }
    get<MyLocation> {
      call.respondText("Location: name=${it.name}, arg1=${it.arg1}, arg2=${it.arg2}")
    }
    // Register nested routes
    get<Type.Edit> {
      call.respondText("Inside $it")
    }
    get<Type.List> {
      call.respondText("Inside $it")
    }
    authenticate("user_session") { }
  }
}

@Location("/location/{name}")
class MyLocation(val name: String, val arg1: Int = 42, val arg2: String = "default")

@Location("/type/{name}")
data class Type(val name: String) {
  @Location("/edit")
  data class Edit(val type: Type)

  @Location("/list/{page}")
  data class List(val type: Type, val page: Int)
}
