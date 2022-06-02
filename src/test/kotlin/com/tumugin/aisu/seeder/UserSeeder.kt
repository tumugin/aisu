package com.tumugin.aisu.seeder

import com.tumugin.aisu.domain.user.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserSeeder : KoinComponent {
  private val userRepository by inject<UserRepository>()

  suspend fun seedUser() {
    userRepository.addUser(
      userName = UserName("藍井すず"),
      userEmail = UserEmail("aoisuzu@example.com"),
      userPassword = UserRawPassword("aoisuzu").toHashedPassword(),
      userEmailVerifiedAt = null,
      userForceLogoutGeneration = UserForceLogoutGeneration(0)
    )
  }
}
