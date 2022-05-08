package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.infra.repository.exposed.models.user.Users
import com.tumugin.aisu.infra.repository.exposed.models.user.User as UserModel

class UserRepositoryImpl : UserRepository {
  override suspend fun getUserById(userId: UserId): User? {
    val rawModel = UserModel.findById(userId.value)
    return if (rawModel != null) {
      toDomain(rawModel)
    } else {
      null
    }
  }

  override suspend fun getUserByEmail(userEmail: UserEmail): User? {
    val rawModel = UserModel.find { Users.email eq userEmail.value }.firstOrNull()
    return if (rawModel != null) {
      toDomain(rawModel)
    } else {
      null
    }
  }

  private fun toDomain(rawModel: UserModel): User {
    return User(
      UserId(rawModel.id.value),
      UserName(rawModel.name),
      UserEmail(rawModel.email),
      UserPassword(rawModel.password),
      rawModel.emailVerifiedAt?.let { UserEmailVerifiedAt(it) },
      UserForceLogoutGeneration(rawModel.userForceLogoutGeneration),
      UserCreatedAt(rawModel.createdAt),
      UserUpdatedAt(rawModel.updatedAt)
    )
  }
}
