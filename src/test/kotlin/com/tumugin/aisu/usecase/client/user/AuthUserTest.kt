package com.tumugin.aisu.usecase.client.user

import com.tumugin.aisu.BaseTest
import com.tumugin.aisu.domain.user.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class AuthUserTest : BaseTest() {
  private val authUser = AuthUser()
  private val userRepository by inject<UserRepository>()

  private suspend fun prepareUser() {
    userRepository.addUser(
      userName = UserName("藍井すず"),
      userEmail = UserEmail("aoisuzu@example.com"),
      userPassword = UserRawPassword("aoisuzu").toHashedPassword(),
      userEmailVerifiedAt = null,
      userForceLogoutGeneration = UserForceLogoutGeneration(0)
    )
  }

  @Test
  fun testAuthAndGetUser() = runTest {
    prepareUser()
    assertNotNull(
      authUser.authAndGetUser(
        UserEmail("aoisuzu@example.com"), UserRawPassword("aoisuzu")
      )
    )
  }

  @Test
  fun testAuthAndGetUserWithInvalidUser() = runTest {
    prepareUser()
    // wrong password
    assertNull(
      authUser.authAndGetUser(
        UserEmail("aoisuzu@example.com"), UserRawPassword("aoisuzuuuuuuuu")
      )
    )
    // wrong email
    assertNull(
      authUser.authAndGetUser(
        UserEmail("aoisuzuuuuuu@example.com"), UserRawPassword("aoisuzu")
      )
    )
    // wrong password and email
    assertNull(
      authUser.authAndGetUser(
        UserEmail("fujimiyamay@example.com"), UserRawPassword("meipomuuuuuu")
      )
    )
  }
}
