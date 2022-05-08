package com.tumugin.aisu.domain.user

interface UserRepository {
  suspend fun getUserById(userId: UserId): User?
  suspend fun getUserByEmail(userEmail: UserEmail): User?
}
