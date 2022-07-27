package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
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

  suspend fun userLogin(@GraphQLIgnore request: ApplicationRequest, params: UserLoginParams): UserSerializer {
    val userLoginRequest = params.toLoginRequest()

    val user = authUserUseCase.authAndGetUser(
      UserEmail(userLoginRequest.email), UserRawPassword(userLoginRequest.password)
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

  class UserLoginParams(val email: String, val password: String) {
    fun toLoginRequest() = LoginRequest(email, password)
  }

  suspend fun userLogout(@GraphQLIgnore request: ApplicationRequest): String {
    request.call.sessions.clear<UserAuthSession>()
    return "Logout success."
  }

  suspend fun userCreate(@GraphQLIgnore request: ApplicationRequest, params: UserCreateParams): UserSerializer {
    val userCreateRequest = params.toCreateUserRequest()
    val user = createUser.createUser(
      userCreateRequest.castedName,
      userCreateRequest.castedEmail,
      userCreateRequest.castedPassword.toHashedPassword(),
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

  class UserCreateParams(val email: String, val password: String, val name: String) {
    fun toCreateUserRequest(): CreateUserRequest {
      return CreateUserRequest(
        email = email,
        password = password,
        name = name
      )
    }
  }
}
