package com.tumugin.aisu.testing.domain.base

import com.tumugin.aisu.domain.base.PaginatorParam
import org.koin.core.component.KoinComponent
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PaginatorParamTest : KoinComponent {
  @Test
  fun testGetOffset() {
    assertEquals(0, PaginatorParam(1, 10).offset)
    assertEquals(10, PaginatorParam(2, 10).offset)
    assertEquals(80, PaginatorParam(9, 10).offset)
    assertEquals(90, PaginatorParam(10, 10).offset)
  }

  @Test
  fun testIllegalParams() {
    assertFailsWith(IllegalArgumentException::class) {
      PaginatorParam(0, 100)
    }
    assertFailsWith(IllegalArgumentException::class) {
      PaginatorParam(100, -1)
    }
  }
}
