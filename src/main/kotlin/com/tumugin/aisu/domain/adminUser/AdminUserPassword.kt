package com.tumugin.aisu.domain.adminUser

import org.mindrot.jbcrypt.BCrypt

@JvmInline
value class AdminUserPassword(val value: String) {
  fun isValid(adminUserRawPassword: AdminUserRawPassword): Boolean {
    return BCrypt.checkpw(adminUserRawPassword.value, this.value)
  }
}
