package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.infra.repository.exposed.models.Users
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel

class UserRepositoryImpl : UserRepository {
  override suspend fun getUserById(userId: UserId): User? {
    return newSuspendedTransaction { UserModel.findById(userId.value)?.toDomain() }
  }

  override suspend fun getUserByIds(userIds: List<UserId>): List<User> {
    return newSuspendedTransaction {
      UserModel.find { Users.id.inList(userIds.map { it.value }) }.map { it.toDomain() }
    }
  }

  override suspend fun getUserByEmail(userEmail: UserEmail): User? {
    return newSuspendedTransaction { UserModel.find { Users.email eq userEmail.value }.firstOrNull()?.toDomain() }
  }

  override suspend fun addUser(
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration,
  ): User {
    return newSuspendedTransaction {
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
      }.toDomain()
    }
  }

  override suspend fun updateUser(
    userId: UserId,
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration
  ): User {
    return newSuspendedTransaction {
      val user = UserModel[userId.value]
      user.name = userName.value
      user.email = userEmail?.value
      user.password = userPassword?.value
      user.emailVerifiedAt = userEmailVerifiedAt?.value
      user.forceLogoutGeneration = userForceLogoutGeneration.value
      user.toDomain()
    }
  }

  override suspend fun deleteUser(userId: UserId) {
    newSuspendedTransaction {
      val user = UserModel[userId.value]
      user.delete()
    }
  }

  override suspend fun getAllUsers(paginatorParam: PaginatorParam): PaginatorResult<User> {
    return newSuspendedTransaction {
      val query = UserModel.all()
      val count = query.count()
      val results = query.limit(paginatorParam.limit.toInt(), paginatorParam.offset).map { it.toDomain() }
      paginatorParam.createPaginatorResult(
        count,
        results
      )
    }
  }
}
