package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.user.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserSeeder : KoinComponent {
  private val userRepository by inject<UserRepository>()

  suspend fun seedUser(
    userName: UserName = UserName("藍井すず"),
    userEmail: UserEmail = UserEmail("aoisuzu@example.com"),
    userRawPassword: UserRawPassword = UserRawPassword("aoisuzu"),
    userEmailVerifiedAt: UserEmailVerifiedAt? = null,
    userForceLogoutGeneration: UserForceLogoutGeneration = UserForceLogoutGeneration(0)
  ): User {
    return userRepository.addUser(
      userName,
      userEmail,
      userRawPassword.toHashedPassword(),
      userEmailVerifiedAt,
      userForceLogoutGeneration
    )
  }
}
