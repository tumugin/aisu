package com.tumugin.aisu.testing.usecase.client.idol

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.idol.GetIdol
import com.tumugin.aisu.usecase.client.idol.WriteIdol
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateIdolTest : BaseDatabaseTest() {
  private val getIdol = GetIdol()
  private val writeIdol = WriteIdol()
  private lateinit var user: User
  private lateinit var idol: Idol

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    idol = IdolSeeder().seedIdol(user.userId)
  }

  @Test
  fun testUpdateIdol() = runTest {
    val updatedIdol = writeIdol.updateIdol(
      idol.idolId,
      user.userId,
      IdolName("小波もも"),
      IdolStatus.PUBLIC_ACTIVE
    )
    val retrievedIdol = getIdol.getIdol(user.userId, idol.idolId)
    Assertions.assertEquals(updatedIdol.idolId, retrievedIdol?.idolId)
    Assertions.assertEquals(updatedIdol.idolName, retrievedIdol?.idolName)
    Assertions.assertEquals(updatedIdol.idolStatus, retrievedIdol?.idolStatus)
    Assertions.assertEquals(updatedIdol.user, retrievedIdol?.user)
    Assertions.assertEquals(updatedIdol.userId, retrievedIdol?.userId)
  }
}
