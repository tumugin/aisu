package com.tumugin.aisu.testing.usecase.client.idol

import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.idol.GetIdol
import com.tumugin.aisu.usecase.client.idol.WriteIdol
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddIdolTest : BaseDatabaseTest() {
  private val getIdol = GetIdol()
  private val writeIdol = WriteIdol()
  private lateinit var user: User

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
  }

  @Test
  fun testAddIdol() = runTest {
    val idol = writeIdol.addIdol(
      user.userId,
      IdolName("村崎ゆうな"),
      IdolStatus.PUBLIC_ACTIVE
    )
    val retrievedIdol = getIdol.getIdol(user.userId, idol.idolId)
    Assertions.assertEquals(idol.idolId, retrievedIdol?.idolId)
    Assertions.assertEquals(idol.idolName, retrievedIdol?.idolName)
    Assertions.assertEquals(idol.idolStatus, retrievedIdol?.idolStatus)
    Assertions.assertEquals(idol.user, retrievedIdol?.user)
    Assertions.assertEquals(idol.userId, retrievedIdol?.userId)
  }
}
