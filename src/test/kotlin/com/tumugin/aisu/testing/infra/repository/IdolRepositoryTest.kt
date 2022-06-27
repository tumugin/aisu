package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.idol.IdolRepository
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class IdolRepositoryTest : BaseDatabaseTest() {
  private val idolRepository by inject<IdolRepository>()

  @BeforeEach
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    IdolSeeder().seedIdol(user.userId)
  }

  @Test
  fun testGetIdol() = runTest {
    val idol = idolRepository.getIdol(IdolId(1))!!
    assertEquals("村崎ゆうな", idol.idolName.value)
    assertEquals(IdolStatus.PRIVATE_ACTIVE, idol.idolStatus)
  }

  @Test
  fun testAddIdol() = runTest {
    val idol = idolRepository.addIdol(
      UserId(1),
      IdolName("工藤みか"),
      IdolStatus.PRIVATE_ACTIVE
    )
    val addedIdol = idolRepository.getIdol(idol.idolId)!!
    assertEquals("工藤みか", addedIdol.idolName.value)
    assertEquals(IdolStatus.PRIVATE_ACTIVE, addedIdol.idolStatus)
  }

  @Test
  fun testUpdateIdol() = runTest {
    val newUser = UserSeeder().seedUser(userEmail = UserEmail("test_idol_repository_test@example.com"))
    GroupSeeder().seedGroup(newUser.userId, GroupName("Appare!"))
    idolRepository.updateIdol(
      IdolId(1),
      newUser.userId,
      IdolName("工藤のか"),
      IdolStatus.PUBLIC_ACTIVE
    )
    val updatedIdol = idolRepository.getIdol(IdolId(1))!!
    assertEquals("工藤のか", updatedIdol.idolName.value)
    assertEquals(IdolStatus.PUBLIC_ACTIVE, updatedIdol.idolStatus)
  }

  @Test
  fun testDeleteIdol() = runTest {
    idolRepository.deleteIdol(IdolId(1))
    assertNull(idolRepository.getIdol(IdolId(1)))
  }
}
