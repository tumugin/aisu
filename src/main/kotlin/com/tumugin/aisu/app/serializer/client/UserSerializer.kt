package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.user.User

@kotlinx.serialization.Serializable
data class UserSerializer(
  val userId: Long,
  val userName: String,
  val userEmail: String?,
  val userEmailVerifiedAt: String?,
  val userCreatedAt: String,
  val userUpdatedAt: String
) {
  companion object {
    fun from(user: User): UserSerializer {
      return UserSerializer(
        user.userId.value,
        user.userName.value,
        user.userEmail?.value,
        user.userEmailVerifiedAt?.value.toString(),
        user.userCreatedAt.value.toString(),
        user.userUpdatedAt.value.toString()
      )
    }
  }
}
