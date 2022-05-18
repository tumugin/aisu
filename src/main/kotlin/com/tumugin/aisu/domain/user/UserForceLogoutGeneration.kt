package com.tumugin.aisu.domain.user

@JvmInline
value class UserForceLogoutGeneration(val value: Int) {
  companion object {
    fun createDefault(): UserForceLogoutGeneration {
      return UserForceLogoutGeneration(0)
    }
  }
}
