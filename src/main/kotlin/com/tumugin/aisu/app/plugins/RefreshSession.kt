package com.tumugin.aisu.app.plugins

import io.ktor.server.application.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlinx.datetime.toInstant

fun Application.configureRefreshToken() {
  intercept(ApplicationCallPipeline.Call) {
    proceed()

    val userAuthSession = call.sessions.get<UserAuthSession>() ?: return@intercept
    val parsedValidTime = userAuthSession.validThroughTimestamp.toInstant()

    // デフォルトの有効期限の半分を過ぎていたら、新しいセッションをセットする
    if (Clock.System.now() - parsedValidTime < UserAuthSession.defaultValidDays / 2) {
      val newSession = UserAuthSession(
        userAuthSession.userId,
        Clock.System.now().plus(UserAuthSession.defaultValidDays).toString(),
        userAuthSession.forceLogoutGeneration
      )
      call.sessions.set(newSession)
    }
  }
}

fun Application.configureAdminRefreshToken() {
  intercept(ApplicationCallPipeline.Call) {
    proceed()

    val adminUserAuthSession = call.sessions.get<AdminUserAuthSession>() ?: return@intercept
    val parsedValidTime = adminUserAuthSession.validThroughTimestamp.toInstant()

    // デフォルトの有効期限の半分を過ぎていたら、新しいセッションをセットする
    if (Clock.System.now() - parsedValidTime < AdminUserAuthSession.defaultValidDays / 2) {
      val newSession = AdminUserAuthSession(
        adminUserAuthSession.adminUserId,
        Clock.System.now().plus(AdminUserAuthSession.defaultValidDays).toString(),
        adminUserAuthSession.forceLogoutGeneration
      )
      call.sessions.set(newSession)
    }
  }
}
