package com.tumugin.aisu.usecase.client.user

import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.domain.user.UserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetUser : KoinComponent {
  private val userRepository: UserRepository by inject()

  suspend fun getUserBySessionUserId(sessionUserId: UserId): User {
    return userRepository.getUserById(sessionUserId) ?: throw NotFoundException()
  }
}
