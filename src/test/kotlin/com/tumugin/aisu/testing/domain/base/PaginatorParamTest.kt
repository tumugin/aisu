package com.tumugin.aisu.testing.domain.base

import com.tumugin.aisu.domain.base.PaginatorParam
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.koin.core.component.KoinComponent

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
    assertThrows(IllegalArgumentException::class.java) {
      PaginatorParam(0, 100)
    }
    assertThrows(IllegalArgumentException::class.java) {
      PaginatorParam(100, -1)
    }
  }
}
