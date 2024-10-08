package com.tumugin.aisu.app.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant

fun Application.configureRefreshToken() {
  intercept(ApplicationCallPipeline.Plugins) {
    val userAuthSession = call.sessions.get<UserAuthSession>()

    if (call.request.headers.get("user-agent") == "node") {
      // SSRからのリクエストの場合はクライアント側に持っているCookieの有効期限と内部で持っている有効期限が食い違ってしまうのでセットしない
      return@intercept
    }

    if (userAuthSession != null) {
      val parsedValidTime = userAuthSession.validThroughTimestamp.toInstant()
      // デフォルトの有効期限の半分を過ぎていたら、新しいセッションをセットする
      if (parsedValidTime - Clock.System.now() < UserAuthSession.defaultValidDays / 2) {
        val newSession = UserAuthSession(
          userAuthSession.userId,
          Clock.System.now().plus(UserAuthSession.defaultValidDays).toString(),
          userAuthSession.forceLogoutGeneration
        )
        call.sessions.set(newSession)
      }
    }
  }
}

fun Application.configureAdminRefreshToken() {
  intercept(ApplicationCallPipeline.Plugins) {
    val adminUserAuthSession = call.sessions.get<AdminUserAuthSession>()

    if (call.request.headers.get("user-agent") == "node") {
      // SSRからのリクエストの場合はクライアント側に持っているCookieの有効期限と内部で持っている有効期限が食い違ってしまうのでセットしない
      return@intercept
    }

    if (adminUserAuthSession != null) {
      val parsedValidTime = adminUserAuthSession.validThroughTimestamp.toInstant()
      // デフォルトの有効期限の半分を過ぎていたら、新しいセッションをセットする
      if (parsedValidTime - Clock.System.now() < AdminUserAuthSession.defaultValidDays / 2) {
        val newSession = AdminUserAuthSession(
          adminUserAuthSession.adminUserId,
          Clock.System.now().plus(AdminUserAuthSession.defaultValidDays).toString(),
          adminUserAuthSession.forceLogoutGeneration
        )
        call.sessions.set(newSession)
      }
    }
  }
}
