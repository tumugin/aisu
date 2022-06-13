package com.tumugin.aisu.testing.usecase.client.user

import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.user.AuthUser
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AuthUserTest : BaseDatabaseTest() {
  private val authUser = AuthUser()
  private val userSeeder = UserSeeder()

  @BeforeEach
  fun beforeTest() = runTest {
    userSeeder.seedUser()
  }

  @Test
  fun testAuthAndGetUser() = runTest {
    assertNotNull(
      authUser.authAndGetUser(
        UserEmail("aoisuzu@example.com"), UserRawPassword("aoisuzu")
      )
    )
  }

  @Test
  fun testAuthAndGetUserWithInvalidUser() = runTest {
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
