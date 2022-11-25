package com.tumugin.aisu.domain.app.config

import java.net.URI

@JvmInline
value class AppConfigAdminAppUrl(val value: String) {
  fun isSecure(): Boolean {
    return URI.create(value).scheme == "https"
  }
}
