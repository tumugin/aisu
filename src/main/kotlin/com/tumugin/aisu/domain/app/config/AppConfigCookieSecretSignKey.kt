package com.tumugin.aisu.domain.app.config

@JvmInline
value class AppConfigCookieSecretSignKey(val value: String) {
  internal fun toByteArray(): ByteArray {
    // Keep existing signatures valid by decoding only complete pairs, as Ktor's hex decoder did.
    return ByteArray(value.length / 2) { index ->
      val offset = index * 2
      val high = value[offset].toString().toInt(16)
      val low = value[offset + 1].toString().toInt(16)
      ((high shl 4) or low).toByte()
    }
  }
}
