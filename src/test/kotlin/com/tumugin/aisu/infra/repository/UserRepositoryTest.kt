package com.tumugin.aisu.infra.repository

import com.tumugin.aisu.BaseDatabaseTest
import com.tumugin.aisu.domain.user.*
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import org.koin.core.component.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserRepositoryTest : BaseDatabaseTest() {
  private val userRepository: UserRepository by inject()

  @Test
  fun testGetUserById() = runTest {
    val user = userRepository.getUserById(UserId(1))
    assertUserFujimiya(user!!)
  }

  @Test
  fun testGetUserByEmail() = runTest {
    val user = userRepository.getUserByEmail(UserEmail("may@example.com"))
    assertUserFujimiya(user!!)
  }

  @Test
  fun testAddUser() = runTest {
    userRepository.addUser(
      UserName("橋本あみ"),
      UserEmail("ami@example.com"),
      UserPassword("amichi"),
      UserEmailVerifiedAt(Instant.parse("2022-12-07T12:00:00+09:00")),
      UserForceLogoutGeneration(100)
    )

    val ami = userRepository.getUserByEmail(UserEmail("ami@example.com"))

    assertEquals(
      mutableMapOf<String, Any?>(
        "userId" to 2L,
        "userName" to "橋本あみ",
        "userEmail" to "ami@example.com",
        "userEmailVerifiedAt" to Instant.parse("2022-12-07T12:00:00+09:00"),
        "userForceLogoutGeneration" to 100
      ), mutableMapOf<String, Any?>(
        "userId" to ami?.userId?.value,
        "userName" to ami?.userName?.value,
        "userEmail" to ami?.userEmail?.value,
        "userEmailVerifiedAt" to ami?.userEmailVerifiedAt?.value,
        "userForceLogoutGeneration" to ami?.userForceLogoutGeneration?.value
      )
    )
  }

  @Test
  fun testUpdateUser() = runTest {
    val user = userRepository.getUserById(UserId(1))!!
    userRepository.updateUser(
      user.userId,
      UserName("めーぽむ"),
      UserEmail("maypomu@example.com"),
      UserPassword("maypomupomu"),
      UserEmailVerifiedAt(Instant.parse("2022-12-10T12:00:00+09:00")),
      UserForceLogoutGeneration(110)
    )

    val updatedUser = userRepository.getUserById(UserId(1))!!

    assertEquals(
      mutableMapOf<String, Any?>(
        "userId" to 1L,
        "userName" to "めーぽむ",
        "userEmail" to "maypomu@example.com",
        "userEmailVerifiedAt" to Instant.parse("2022-12-10T12:00:00+09:00"),
        "userForceLogoutGeneration" to 110
      ), mutableMapOf<String, Any?>(
        "userId" to updatedUser.userId.value,
        "userName" to updatedUser.userName.value,
        "userEmail" to updatedUser.userEmail?.value,
        "userEmailVerifiedAt" to updatedUser.userEmailVerifiedAt?.value,
        "userForceLogoutGeneration" to updatedUser.userForceLogoutGeneration.value
      )
    )
  }

  @Test
  fun testDeleteUser() = runTest {
    userRepository.deleteUser(UserId(1))
    assertNull(
      userRepository.getUserById(UserId(1))
    )
  }

  @BeforeTest
  fun prepareTestUser() = runTest {
    userRepository.addUser(
      UserName("藤宮めい"),
      UserEmail("may@example.com"),
      UserPassword("maypomu"),
      UserEmailVerifiedAt(Instant.parse("2022-12-07T12:00:00+09:00")),
      UserForceLogoutGeneration(100)
    )
  }

  private fun assertUserFujimiya(maypomu: User) {
    assertEquals(
      mutableMapOf<String, Any?>(
        "userId" to 1L,
        "userName" to "藤宮めい",
        "userEmail" to "may@example.com",
        "userEmailVerifiedAt" to Instant.parse("2022-12-07T12:00:00+09:00"),
        "userForceLogoutGeneration" to 100
      ), mutableMapOf<String, Any?>(
        "userId" to maypomu.userId.value,
        "userName" to maypomu.userName.value,
        "userEmail" to maypomu.userEmail?.value,
        "userEmailVerifiedAt" to maypomu.userEmailVerifiedAt?.value,
        "userForceLogoutGeneration" to maypomu.userForceLogoutGeneration.value
      )
    )
  }
}
