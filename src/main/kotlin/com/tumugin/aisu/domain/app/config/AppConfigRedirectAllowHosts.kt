package com.tumugin.aisu.domain.app.config

import java.net.URI

class AppConfigRedirectAllowHosts(val value: List<String>) {
  companion object {
    fun fromRawValue(rawValue: String): AppConfigRedirectAllowHosts {
      return AppConfigRedirectAllowHosts(rawValue.split(",").map { it.trim() }.filter { it.isNotEmpty() })
    }
  }

  fun contains(host: String): Boolean {
    return value.contains(host)
  }

  fun isAllowedByPath(path: String): Boolean {
    val uri = URI.create(path)
    if (uri.host == null) {
      return true
    }
    return contains(uri.host)
  }
}
