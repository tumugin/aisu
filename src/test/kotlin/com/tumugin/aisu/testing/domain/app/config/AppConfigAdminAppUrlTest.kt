package com.tumugin.aisu.testing.domain.app.config

import com.tumugin.aisu.domain.app.config.AppConfigAdminAppUrl
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AppConfigAdminAppUrlTest {
  @ParameterizedTest
  @ValueSource(
    strings = ["https://example.com", "https://example.com/"]
  )
  fun testIsSecureTrue(url: String) {
    Assertions.assertTrue(AppConfigAdminAppUrl(url).isSecure())
  }

  @ParameterizedTest
  @ValueSource(
    strings = ["http://example.com", "http://example.com/", "/"]
  )
  fun testIsSecureFalse(url: String) {
    Assertions.assertFalse(AppConfigAdminAppUrl(url).isSecure())
  }
}
