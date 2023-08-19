package com.tumugin.aisu.usecase.client.user

import com.tumugin.aisu.domain.exception.InvalidContextException
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.domain.user.UserName
import com.tumugin.aisu.domain.user.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateUserName : KoinComponent {
  private val userRepository: UserRepository by inject()

  suspend fun updateUserName(userId: UserId, userName: UserName) {
    val currentUser = userRepository.getUserById(userId) ?: throw InvalidContextException()
    userRepository.updateUser(
      userId = userId,
      userName = userName,
      userEmail = currentUser.userEmail,
      userPassword = currentUser.userPassword,
      userEmailVerifiedAt = currentUser.userEmailVerifiedAt,
      userForceLogoutGeneration = currentUser.userForceLogoutGeneration,
    )
  }
}
