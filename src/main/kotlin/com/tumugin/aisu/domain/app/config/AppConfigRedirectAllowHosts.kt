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
    // リダイレクト先のhostが無い時は必然的に同ホストへ飛ばすので許可する
    if (uri.host == null) {
      return true
    }
    return contains(uri.host)
  }
}
