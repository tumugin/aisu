package com.tumugin.aisu.testing.domain.app.config

import com.tumugin.aisu.domain.app.config.AppConfigCORSAllowHosts
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent

class AppConfigCORSAllowHostsTest : KoinComponent {
  @Test
  fun testFromRawValueWithEmpty() {
    Assertions.assertEquals(0, AppConfigCORSAllowHosts.fromRawValue("").value.size)
  }

  @Test
  fun testFromRawValue() {
    Assertions.assertEquals(
      listOf("hoge", "fuga", "piyo"), AppConfigCORSAllowHosts.fromRawValue("hoge, fuga, piyo").value
    )
    Assertions.assertEquals(
      listOf("hoge", "fuga", "piyo"), AppConfigCORSAllowHosts.fromRawValue("hoge,fuga,piyo").value
    )
    Assertions.assertEquals(
      listOf("hoge", "fuga", "piyo"), AppConfigCORSAllowHosts.fromRawValue("hoge,fuga,piyo,").value
    )
  }
}
