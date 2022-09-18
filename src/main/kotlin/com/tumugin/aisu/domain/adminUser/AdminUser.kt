package com.tumugin.aisu.domain.adminUser

data class AdminUser(
  val adminUserId: AdminUserId,
  val adminUserName: AdminUserName,
  val adminUserEmail: AdminUserEmail,
  val adminUserPassword: AdminUserPassword,
  val adminUserForceLogoutGeneration: AdminUserForceLogoutGeneration,
  val adminUserCreatedAt: AdminUserCreatedAt,
  val adminUserUpdatedAt: AdminUserUpdatedAt
)
