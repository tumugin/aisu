package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.CreateUserRequest
import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.app.serializer.client.UserSerializer
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserForceLogoutGeneration
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.usecase.client.user.AuthUser
import com.tumugin.aisu.usecase.client.user.CreateUser
import graphql.GraphQLException
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class UserMutationService : Mutation {
  private val authUserUseCase = AuthUser()
  private val createUser = CreateUser()

  suspend fun userLogin(request: ApplicationRequest, params: LoginRequest): UserSerializer {
    val user = authUserUseCase.authAndGetUser(
      UserEmail(params.email), UserRawPassword(params.password)
    ) ?: throw GraphQLException("Login failed.")

    request.call.sessions.set(
      UserAuthSession(
        userId = user.userId.value,
        validThroughTimestamp = Clock.System.now().plus(30.days).toString(),
        forceLogoutGeneration = user.userForceLogoutGeneration.value
      )
    )

    return UserSerializer.from(user)
  }

  suspend fun userLogout(request: ApplicationRequest): Unit? {
    request.call.sessions.clear<UserAuthSession>()

    return null
  }

  suspend fun userCreate(request: ApplicationRequest, params: CreateUserRequest): UserSerializer {
    val user = createUser.createUser(
      params.castedName,
      params.castedEmail,
      params.castedPassword.toHashedPassword(),
      null,
      UserForceLogoutGeneration(0)
    )

    request.call.sessions.set(
      UserAuthSession(
        userId = user.userId.value,
        validThroughTimestamp = Clock.System.now().plus(30.days).toString(),
        forceLogoutGeneration = user.userForceLogoutGeneration.value
      )
    )

    return UserSerializer.from(user)
  }
}
