package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserSerializer(
  @Serializable(with = IDSerializer::class)
  val userId: ID,
  val userName: String,
  val userEmail: String?,
  val userEmailVerifiedAt: String?,
  val userCreatedAt: String,
  val userUpdatedAt: String
) {
  companion object {
    fun from(user: User): UserSerializer {
      return UserSerializer(
        ID(user.userId.value.toString()),
        user.userName.value,
        user.userEmail?.value,
        user.userEmailVerifiedAt?.let { it.value.toString() },
        user.userCreatedAt.value.toString(),
        user.userUpdatedAt.value.toString()
      )
    }
  }
}
