package com.tumugin.aisu.testing.usecase.client.idol

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.idol.GetIdol
import com.tumugin.aisu.usecase.client.idol.WriteIdol
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteIdolTest : BaseDatabaseTest() {
  private val getIdol = GetIdol()
  private val writeIdol = WriteIdol()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var idol: Idol

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
  }

  @Test
  fun testDeleteIdol() = runTest {
    writeIdol.deleteIdol(user.userId, idol.idolId)
    Assertions.assertNull(getIdol.getIdol(user.userId, idol.idolId))
  }

  @Test
  fun testUpdateIdolWithNoPermissionUser() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeIdol.deleteIdol(userTwo.userId, idol.idolId)
      }
    }
  }
}
