@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.testing.usecase.client.user

import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.usecase.client.user.CreateUser
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertThrows
import org.koin.test.inject

class CreateUserTest : BaseDatabaseTest() {
  private val createUser = CreateUser()
  private val userRepository by inject<UserRepository>()

  private suspend fun createAoiSuzu(): User {
    return createUser.createUser(
      UserName("藍井すず"),
      UserEmail("aoisuzu@example.com"),
      UserRawPassword("aoisuzuuuu").toHashedPassword(),
      null,
      UserForceLogoutGeneration.createDefault()
    )
  }

  @Test
  fun testCreateUser() = runTest {
    createAoiSuzu()
    val result = userRepository.getUserByEmail(UserEmail("aoisuzu@example.com"))
    assertEquals(
      mutableMapOf<String, Any?>(
        "userId" to 1L,
        "userEmail" to "aoisuzu@example.com",
        "userEmailVerifiedAt" to null,
        "userForceLogoutGeneration" to 0
      ), mutableMapOf<String, Any?>(
        "userId" to result?.userId?.value,
        "userEmail" to result?.userEmail?.value,
        "userEmailVerifiedAt" to result?.userEmailVerifiedAt?.value,
        "userForceLogoutGeneration" to result?.userForceLogoutGeneration?.value
      )
    )
  }

  @Test
  fun testDuplicateEmailUser() = runTest {
    createAoiSuzu()
    assertThrows(UserAlreadyExistException::class.java) {
      runBlocking {
        createUser.createUser(
          UserName("すずあおい"),
          UserEmail("aoisuzu@example.com"),
          UserRawPassword("suzuaoi").toHashedPassword(),
          null,
          UserForceLogoutGeneration.createDefault()
        )
      }
    }
  }
}
