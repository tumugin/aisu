package com.tumugin.aisu.testing.usecase.client.idol

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.idol.GetIdol
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetIdolTest : BaseDatabaseTest() {
  private val getIdol = GetIdol()
  private lateinit var user: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE"])
  fun testGetPrivateIdol(status: String) = runTest {
    val idol = IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.valueOf(status))
    val retrievedIdol = getIdol.getIdol(user.userId, idol.idolId)
    Assertions.assertEquals(idol, retrievedIdol)
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        getIdol.getIdol(userTwo.userId, idol.idolId)
      }
    }
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE"])
  fun testGetPublicIdol(status: String) = runTest {
    val idol = IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.valueOf(status))
    val retrievedIdol = getIdol.getIdol(user.userId, idol.idolId)
    Assertions.assertEquals(idol, retrievedIdol)
    val retrievedIdolByTwo = getIdol.getIdol(userTwo.userId, idol.idolId)
    Assertions.assertEquals(idol, retrievedIdolByTwo)
  }

  @Test
  fun testGetDeletedIdol() = runTest {
    val idol = IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.OPERATION_DELETED)
    val retrievedIdol = getIdol.getIdol(user.userId, idol.idolId)
    Assertions.assertEquals(idol, retrievedIdol)
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        getIdol.getIdol(userTwo.userId, idol.idolId)
      }
    }
  }
}
