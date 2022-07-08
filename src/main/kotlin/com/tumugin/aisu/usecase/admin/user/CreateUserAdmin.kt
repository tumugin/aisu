package com.tumugin.aisu.usecase.admin.user

import com.tumugin.aisu.domain.user.*
import org.koin.core.component.KoinComponent

class CreateUserAdmin : KoinComponent {
  private val userDomainService = UserDomainService()

  suspend fun createUser(
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration
  ): User {
    return userDomainService.createUser(
      userName,
      userEmail,
      userPassword,
      userEmailVerifiedAt,
      userForceLogoutGeneration
    )
  }
}
