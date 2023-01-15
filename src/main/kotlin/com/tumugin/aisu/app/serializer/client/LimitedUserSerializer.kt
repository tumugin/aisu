package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.user.LimitedUser
import kotlinx.serialization.Serializable

@Serializable
class LimitedUserSerializer(
  @Serializable(with = IDSerializer::class)
  val userId: ID,
  val userName: String,
) {
  companion object {
    fun from(limitedUser: LimitedUser): LimitedUserSerializer {
      return LimitedUserSerializer(
        userId = ID(limitedUser.userId.value.toString()),
        userName = limitedUser.userName.value
      )
    }
  }
}
