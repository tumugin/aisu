package com.tumugin.aisu.testing.domain.app.config

import com.tumugin.aisu.domain.app.config.AppConfigRedirectAllowHosts
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class AppConfigRedirectAllowHostsTest {
  @Test
  fun testFromRawValueWithEmpty() {
    Assertions.assertEquals(0, AppConfigRedirectAllowHosts.fromRawValue("").value.size)
  }

  @Test
  fun testFromRawValue() {
    Assertions.assertEquals(
      listOf("hoge", "fuga", "piyo"), AppConfigRedirectAllowHosts.fromRawValue("hoge, fuga, piyo").value
    )
    Assertions.assertEquals(
      listOf("hoge", "fuga", "piyo"), AppConfigRedirectAllowHosts.fromRawValue("hoge,fuga,piyo").value
    )
    Assertions.assertEquals(
      listOf("hoge", "fuga", "piyo"), AppConfigRedirectAllowHosts.fromRawValue("hoge,fuga,piyo,").value
    )
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "https://example.com",
      "https://example.com/test",
      "/hoge/hoge"
    ]
  )
  fun testIsAllowedByPathTrueCase(testPath: String) {
    val allowHosts = AppConfigRedirectAllowHosts(listOf("example.com"))
    Assertions.assertTrue(allowHosts.isAllowedByPath(testPath))
  }

  @ParameterizedTest
  @ValueSource(
    strings = [
      "https://example2.com",
      "https://example2.com/test"
    ]
  )
  fun testIsAllowedByPathFalseCase(testPath: String) {
    val allowHosts = AppConfigRedirectAllowHosts(listOf("example.com"))
    Assertions.assertFalse(allowHosts.isAllowedByPath(testPath))
  }
}
