package com.tumugin.aisu.testing.infra.app.csrf

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import com.tumugin.aisu.testing.BaseDatabaseTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CSRFRepositoryTest : BaseDatabaseTest() {
  private val csrfRepository by inject<CSRFRepository>()

  @Test
  fun testGenerateToken() = runTest {
    val generatedToken = csrfRepository.generateToken()
    assertTrue(generatedToken.value.isNotEmpty())
    assertTrue(generatedToken.value.length > 10)
  }

  @Test
  fun testValidateTokenExistsWithExistingToken() = runTest {
    val generatedToken = csrfRepository.generateToken()
    assertTrue(csrfRepository.validateTokenExists(generatedToken))
  }

  @Test
  fun testValidateTokenExistsWithNotExistingToken() = runTest {
    val token = CSRFToken("naruse-mirua")
    assertFalse(csrfRepository.validateTokenExists(token))
  }
}
