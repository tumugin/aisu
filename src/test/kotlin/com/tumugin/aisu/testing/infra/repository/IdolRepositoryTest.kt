package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.group.GroupId
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
import org.koin.core.component.inject
import kotlin.test.*

class IdolRepositoryTest : BaseDatabaseTest() {
  private val idolRepository by inject<IdolRepository>()

  @BeforeTest
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    IdolSeeder().seedIdol(GroupSeeder().seedGroup(user.userId).groupId, user.userId)
  }

  @Test
  fun testGetIdol() = runTest {
    val idol = idolRepository.getIdol(IdolId(1))!!
    assertNotNull(idol.group)
    assertNotNull(idol.user)
    assertEquals("村崎ゆうな", idol.idolName.value)
    assertEquals(IdolStatus.PRIVATE_ACTIVE, idol.idolStatus)
  }

  @Test
  fun testAddIdol() = runTest {
    val idol = idolRepository.addIdol(
      GroupId(1),
      UserId(1),
      IdolName("工藤みか"),
      IdolStatus.PRIVATE_ACTIVE
    )
    val addedIdol = idolRepository.getIdol(idol.idolId)!!
    assertNotNull(addedIdol.group)
    assertNotNull(addedIdol.user)
    assertEquals("工藤みか", addedIdol.idolName.value)
    assertEquals(IdolStatus.PRIVATE_ACTIVE, addedIdol.idolStatus)
  }

  @Test
  fun testUpdateIdol() = runTest {
    val newUser = UserSeeder().seedUser(userEmail = UserEmail("test_idol_repository_test@example.com"))
    val newGroup = GroupSeeder().seedGroup(newUser.userId, GroupName("Appare!"))
    idolRepository.updateIdol(
      IdolId(1),
      newGroup.groupId,
      newUser.userId,
      IdolName("工藤のか"),
      IdolStatus.PUBLIC_ACTIVE
    )
    val updatedIdol = idolRepository.getIdol(IdolId(1))!!
    assertEquals(newUser, updatedIdol.user)
    assertEquals(newGroup, updatedIdol.group)
    assertEquals("工藤のか", updatedIdol.idolName.value)
    assertEquals(IdolStatus.PUBLIC_ACTIVE, updatedIdol.idolStatus)
  }

  @Test
  fun testDeleteIdol() = runTest {
    idolRepository.deleteIdol(IdolId(1))
    assertNull(idolRepository.getIdol(IdolId(1)))
  }
}
