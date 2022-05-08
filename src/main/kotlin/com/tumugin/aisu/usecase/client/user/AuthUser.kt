package com.tumugin.aisu.usecase.client.user

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.domain.user.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthUser : KoinComponent {
  private val userRepository: UserRepository by inject()

  suspend fun authAndGetUser(userEmail: UserEmail, userRawPassword: UserRawPassword): User? {
    val user = userRepository.getUserByEmail(userEmail)
    return if (user?.userPassword?.isValid(userRawPassword) == true) {
      user
    } else {
      null
    }
  }
}
