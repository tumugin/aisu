package com.tumugin.aisu.domain.adminUser

import org.mindrot.jbcrypt.BCrypt

@JvmInline
value class AdminUserRawPassword(val value: String) {
  fun toHashedPassword(): AdminUserPassword {
    return AdminUserPassword(BCrypt.hashpw(this.value, BCrypt.gensalt()))
  }
}
