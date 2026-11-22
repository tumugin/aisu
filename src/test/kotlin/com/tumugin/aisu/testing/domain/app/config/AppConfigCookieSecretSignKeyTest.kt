package com.tumugin.aisu.testing.domain.app.config

import com.tumugin.aisu.domain.app.config.AppConfigCookieSecretSignKey
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AppConfigCookieSecretSignKeyTest {
  @ParameterizedTest
  @ValueSource(strings = ["0AbF", "0AbF1", "0AbF!", "０ＡｂＦ"])
  fun testDecodesSameBytesAsKtor(key: String) {
    assertArrayEquals(byteArrayOf(0x0a, 0xbf.toByte()), AppConfigCookieSecretSignKey(key).toByteArray())
  }

  @ParameterizedTest
  @ValueSource(strings = ["", "f"])
  fun testKeysWithoutCompletePairsDecodeToEmptyArray(key: String) {
    assertArrayEquals(byteArrayOf(), AppConfigCookieSecretSignKey(key).toByteArray())
  }

  @Test
  fun testRejectsInvalidCharactersInCompletePairs() {
    assertThrows(NumberFormatException::class.java) {
      AppConfigCookieSecretSignKey("0!").toByteArray()
    }
  }
}
