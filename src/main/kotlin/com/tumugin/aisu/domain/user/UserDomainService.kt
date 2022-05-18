package com.tumugin.aisu.domain.user

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserDomainService : KoinComponent {
  private val userRepository by inject<UserRepository>()

  suspend fun createUser(
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration
  ): User {
    val existingUser = userEmail?.let { userRepository.getUserByEmail(it) }
    if (existingUser != null) {
      throw UserAlreadyExistException()
    }
    return userRepository.addUser(
      userName,
      userEmail,
      userPassword,
      userEmailVerifiedAt,
      userForceLogoutGeneration
    )
  }
}
