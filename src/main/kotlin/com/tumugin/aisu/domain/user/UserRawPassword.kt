package com.tumugin.aisu.domain.user

import org.mindrot.jbcrypt.BCrypt

@JvmInline
value class UserRawPassword(val value: String) {
  fun toHashedPassword(): UserPassword {
    return UserPassword(BCrypt.hashpw(this.value, BCrypt.gensalt()))
  }
}
