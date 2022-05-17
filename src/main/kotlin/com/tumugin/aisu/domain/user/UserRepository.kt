package com.tumugin.aisu.domain.user

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
}
