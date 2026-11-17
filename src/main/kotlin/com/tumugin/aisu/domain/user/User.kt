package com.tumugin.aisu.domain.user

import kotlin.time.Clock
import kotlin.time.Instant

data class User(
  val userId: UserId,
  val userName: UserName,
  val userEmail: UserEmail?,
  val userPassword: UserPassword?,
  val userEmailVerifiedAt: UserEmailVerifiedAt?,
  val userForceLogoutGeneration: UserForceLogoutGeneration,
  val userCreatedAt: UserCreatedAt,
  val userUpdatedAt: UserUpdatedAt
) {
  fun canLoginFromSessionParams(validThroughTimestamp: String, forceLogoutGeneration: Int): Boolean {
    return Instant.parse(validThroughTimestamp) > Clock.System.now() &&
      userForceLogoutGeneration.value == forceLogoutGeneration
  }
}
