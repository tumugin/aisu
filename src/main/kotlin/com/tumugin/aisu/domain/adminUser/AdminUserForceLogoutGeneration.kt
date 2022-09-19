package com.tumugin.aisu.domain.adminUser

@JvmInline
value class AdminUserForceLogoutGeneration(val value: Int) {
  companion object {
    fun createDefault(): AdminUserForceLogoutGeneration {
      return AdminUserForceLogoutGeneration(0)
    }
  }
}
