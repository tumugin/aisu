package com.tumugin.aisu.testing.usecase.admin.idol

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.idol.GetIdolAdmin
import com.tumugin.aisu.usecase.admin.idol.WriteIdolAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteIdolTest : BaseDatabaseTest() {
  private val getIdolAdmin = GetIdolAdmin()
  private val writeIdolAdmin = WriteIdolAdmin()
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
    writeIdolAdmin.deleteIdol(idol.idolId)
    Assertions.assertNull(getIdolAdmin.getIdol(idol.idolId))
  }

  @Test
  fun testUpdateIdolWithNoPermissionUser() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeIdolAdmin.deleteIdol(idol.idolId)
      }
    }
  }
}
