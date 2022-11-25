package com.tumugin.aisu.testing.domain.app.config

import com.tumugin.aisu.domain.app.config.AppConfigAppUrl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AppConfigAppUrlTest {
  @ParameterizedTest
  @ValueSource(
    strings = ["https://example.com", "https://example.com/"]
  )
  fun testIsSecureTrue(url: String) {
    Assertions.assertTrue(AppConfigAppUrl(url).isSecure())
  }

  @ParameterizedTest
  @ValueSource(
    strings = ["http://example.com", "http://example.com/", "/"]
  )
  fun testIsSecureFalse(url: String) {
    Assertions.assertFalse(AppConfigAppUrl(url).isSecure())
  }
}
