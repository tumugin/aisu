package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.CreateUserRequest
import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.app.serializer.client.UserSerializer
import com.tumugin.aisu.domain.exception.LoginFailedException
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserForceLogoutGeneration
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.usecase.client.user.AuthUser
import com.tumugin.aisu.usecase.client.user.CreateUser
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class UserMutationService : Mutation {
  private val authUserUseCase = AuthUser()
  private val createUser = CreateUser()

  suspend fun userLogin(dfe: DataFetchingEnvironment, params: UserLoginParams): UserSerializer {
    val request = dfe.graphQlContext.get<ApplicationRequest>(ApplicationRequest::class)
    val userLoginRequest = params.toLoginRequest()

    val user = authUserUseCase.authAndGetUser(
      UserEmail(userLoginRequest.email), UserRawPassword(userLoginRequest.password)
    ) ?: throw LoginFailedException("Login failed. Wrong password or email.")

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

  suspend fun userLogout(dfe: DataFetchingEnvironment): String {
    val request = dfe.graphQlContext.get<ApplicationRequest>(ApplicationRequest::class)
    request.call.sessions.clear<UserAuthSession>()
    return "Logout success."
  }

  suspend fun userCreate(dfe: DataFetchingEnvironment, params: UserCreateParams): UserSerializer {
    val request = dfe.graphQlContext.get<ApplicationRequest>(ApplicationRequest::class)
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
