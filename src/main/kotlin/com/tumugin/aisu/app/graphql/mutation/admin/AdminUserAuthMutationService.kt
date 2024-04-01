package com.tumugin.aisu.app.graphql.mutation.admin

import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.params.admin.AdminUserLoginParams
import com.tumugin.aisu.app.plugins.AdminUserAuthSession
import com.tumugin.aisu.app.serializer.admin.AdminUserSerializer
import com.tumugin.aisu.domain.adminUser.AdminUserEmail
import com.tumugin.aisu.domain.adminUser.AdminUserRawPassword
import com.tumugin.aisu.domain.exception.LoginFailedException
import com.tumugin.aisu.usecase.admin.adminUser.AuthAdminUser
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class AdminUserAuthMutationService : Mutation {
  private val authAdminUser = AuthAdminUser()

  suspend fun adminUserLogin(dfe: DataFetchingEnvironment, params: AdminUserLoginParams): AdminUserSerializer {
    val request = dfe.graphQlContext.get<ApplicationRequest>(ApplicationRequest::class)

    val adminUser = authAdminUser.authAndGetAdminUser(
      AdminUserEmail(params.email), AdminUserRawPassword(params.password)
    ) ?: throw LoginFailedException("Login failed. Wrong password or email.")

    request.call.sessions.set(
      AdminUserAuthSession(
        adminUserId = adminUser.adminUserId.value,
        validThroughTimestamp = Clock.System.now().plus(AdminUserAuthSession.defaultValidDays).toString(),
        forceLogoutGeneration = adminUser.adminUserForceLogoutGeneration.value
      )
    )

    return AdminUserSerializer.from(adminUser)
  }

  suspend fun adminUserLogout(dfe: DataFetchingEnvironment): String {
    val request = dfe.graphQlContext.get<ApplicationRequest>(ApplicationRequest::class)
    request.call.sessions.clear<AdminUserAuthSession>()
    return "Logout success."
  }
}
