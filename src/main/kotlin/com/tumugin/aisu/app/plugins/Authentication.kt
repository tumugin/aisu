package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.domain.user.UserRepository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import org.koin.core.Koin

fun Application.configureAuthentication(koin: Koin) {
  val userRepository = koin.get<UserRepository>()
  install(Authentication) {
    session<UserAuthSession>("user_session") {
      challenge {
        call.respondRedirect("/api/login")
      }
      validate { session ->
        val user = userRepository.getUserById(UserId(session.userId))
        return@validate if (user?.canLoginFromSessionParams(
            session.validThroughTimestamp, session.forceLogoutGeneration
          ) == false
        ) {
          // セッションからのログイン不許可の場合
          null
        } else if (user != null) {
          // 許可の場合
          UserPrincipal(user)
        } else {
          // ユーザが存在しない場合
          null
        }
      }
    }
  }
}
