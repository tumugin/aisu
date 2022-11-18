package com.tumugin.aisu.domain.app.config

@JvmInline
value class AppConfigCORSAllowHosts(val value: List<String>) {
  companion object {
    fun fromRawValue(rawValue: String): AppConfigCORSAllowHosts {
      return AppConfigCORSAllowHosts(rawValue.split(",").map { it.trim() })
    }
  }
}
