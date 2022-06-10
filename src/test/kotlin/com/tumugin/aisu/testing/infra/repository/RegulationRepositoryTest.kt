package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import kotlin.test.*

class RegulationRepositoryTest : BaseDatabaseTest() {
  private val regulationRepository by inject<RegulationRepository>()

  @BeforeTest
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    RegulationSeeder().seedRegulation(
      GroupSeeder().seedGroup(user.userId).groupId,
      user.userId
    )
  }

  @Test
  fun testGetRegulation() = runTest {
    val result = regulationRepository.getRegulation(RegulationId(1))
    assertNotNull(result)
  }

  @Test
  fun testDeleteRegulation() = runTest {
    regulationRepository.deleteRegulation(RegulationId(1))
    assertNull(regulationRepository.getRegulation(RegulationId(1)))
  }

  @Test
  fun testAddRegulation() = runTest {
    val expected = regulationRepository.addRegulation(
      GroupId(1),
      UserId(1),
      RegulationName("Appare!券"),
      RegulationComment("テスト"),
      RegulationUnitPrice(2000),
      RegulationStatus.ACTIVE
    )
    val actual = regulationRepository.getRegulation(expected.regulationId)!!
    assertEquals(expected.groupId, actual.groupId)
    assertEquals(expected.userId, actual.userId)
    assertEquals(expected.regulationName, actual.regulationName)
    assertEquals(expected.regulationComment, actual.regulationComment)
    assertEquals(expected.regulationUnitPrice, actual.regulationUnitPrice)
    assertEquals(expected.regulationStatus, actual.regulationStatus)
  }

  @Test
  fun testUpdateRegulation() = runTest {
    val user = UserSeeder().seedUser(userEmail = UserEmail("test_update_regulation@example.com"))
    val expected = regulationRepository.updateRegulation(
      RegulationId(1),
      GroupSeeder().seedGroup(user.userId).groupId,
      user.userId,
      RegulationName("ほげほげ"),
      RegulationComment("テストああああ"),
      RegulationUnitPrice(2000),
      RegulationStatus.ACTIVE
    )
    val actual = regulationRepository.getRegulation(expected.regulationId)!!
    assertEquals(expected.groupId, actual.groupId)
    assertEquals(expected.userId, actual.userId)
    assertEquals(expected.regulationName, actual.regulationName)
    assertEquals(expected.regulationComment, actual.regulationComment)
    assertEquals(expected.regulationUnitPrice, actual.regulationUnitPrice)
    assertEquals(expected.regulationStatus, actual.regulationStatus)
  }
}
