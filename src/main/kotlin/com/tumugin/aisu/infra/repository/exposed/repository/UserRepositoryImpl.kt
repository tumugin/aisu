package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.infra.repository.exposed.models.Users
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel

class UserRepositoryImpl : UserRepository {
  override suspend fun getUserById(userId: UserId): User? {
    val rawModel = transaction { UserModel.findById(userId.value) }
    return if (rawModel != null) {
      toDomain(rawModel)
    } else {
      null
    }
  }

  override suspend fun getUserByEmail(userEmail: UserEmail): User? {
    val rawModel = transaction { UserModel.find { Users.email eq userEmail.value }.firstOrNull() }
    return if (rawModel != null) {
      toDomain(rawModel)
    } else {
      null
    }
  }

  override suspend fun addUser(
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration,
  ): User {
    val createdModel = transaction {
      UserModel.new {
        name = userName.value
        if (userEmail != null) {
          email = userEmail.value
        }
        if (userPassword != null) {
          password = userPassword.value
        }
        if (userEmailVerifiedAt != null) {
          emailVerifiedAt = userEmailVerifiedAt.value
        }
        this.forceLogoutGeneration = userForceLogoutGeneration.value
      }
    }

    return toDomain(createdModel)
  }

  override suspend fun updateUser(
    userId: UserId,
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration
  ): User {
    return transaction {
      val user = UserModel[userId.value]
      user.name = userName.value
      user.email = userEmail?.value
      user.password = userPassword?.value
      user.emailVerifiedAt = userEmailVerifiedAt?.value
      user.forceLogoutGeneration = userForceLogoutGeneration.value
      toDomain(user)
    }
  }

  override suspend fun deleteUser(userId: UserId) {
    transaction {
      val user = UserModel[userId.value]
      user.delete()
    }
  }

  private fun toDomain(rawModel: UserModel): User {
    return User(
      UserId(rawModel.id.value),
      UserName(rawModel.name),
      rawModel.email?.let { UserEmail(it) },
      rawModel.password?.let { UserPassword(it) },
      rawModel.emailVerifiedAt?.let { UserEmailVerifiedAt(it) },
      UserForceLogoutGeneration(rawModel.forceLogoutGeneration),
      UserCreatedAt(rawModel.createdAt),
      UserUpdatedAt(rawModel.updatedAt)
    )
  }
}
