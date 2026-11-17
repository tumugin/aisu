package com.tumugin.aisu.domain.adminUser

import kotlin.time.Clock
import kotlin.time.Instant

data class AdminUser(
  val adminUserId: AdminUserId,
  val adminUserName: AdminUserName,
  val adminUserEmail: AdminUserEmail,
  val adminUserPassword: AdminUserPassword?,
  val adminUserForceLogoutGeneration: AdminUserForceLogoutGeneration,
  val adminUserCreatedAt: AdminUserCreatedAt,
  val adminUserUpdatedAt: AdminUserUpdatedAt
) {
  fun canLoginFromSessionParams(validThroughTimestamp: String, forceLogoutGeneration: Int): Boolean {
    return Instant.parse(validThroughTimestamp) > Clock.System.now() &&
      adminUserForceLogoutGeneration.value == forceLogoutGeneration
  }
}
