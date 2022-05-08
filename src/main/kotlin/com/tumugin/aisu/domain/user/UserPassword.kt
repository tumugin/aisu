package com.tumugin.aisu.domain.user

import org.mindrot.jbcrypt.BCrypt

@JvmInline
value class UserPassword(val value: String) {
  fun isValid(userRawPassword: UserRawPassword): Boolean {
    return BCrypt.checkpw(userRawPassword.value, this.value)
  }
}
