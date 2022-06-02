package com.tumugin.aisu.usecase.client.user

import com.tumugin.aisu.BaseDatabaseTest
import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.seeder.UserSeeder
import kotlinx.coroutines.test.runTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuthUserTest : BaseDatabaseTest() {
  private val authUser = AuthUser()
  private val userSeeder = UserSeeder()

  @BeforeTest
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
