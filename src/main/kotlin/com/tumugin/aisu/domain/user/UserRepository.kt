package com.tumugin.aisu.domain.user

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult

interface UserRepository {
  suspend fun getUserById(userId: UserId): User?
  suspend fun getUserByEmail(userEmail: UserEmail): User?
  suspend fun addUser(
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration,
  ): User

  suspend fun updateUser(
    userId: UserId,
    userName: UserName,
    userEmail: UserEmail?,
    userPassword: UserPassword?,
    userEmailVerifiedAt: UserEmailVerifiedAt?,
    userForceLogoutGeneration: UserForceLogoutGeneration,
  ): User

  suspend fun deleteUser(userId: UserId)

  suspend fun getAllUsers(paginatorParam: PaginatorParam): PaginatorResult<User>
}
