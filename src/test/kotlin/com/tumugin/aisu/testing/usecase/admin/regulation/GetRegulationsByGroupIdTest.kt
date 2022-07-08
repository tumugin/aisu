package com.tumugin.aisu.testing.usecase.admin.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.regulation.RegulationStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.regulation.GetRegulationAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRegulationsByGroupIdTest : BaseDatabaseTest() {
  private val getRegulationAdmin = GetRegulationAdmin()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var group: Group
  private lateinit var regulationActive: Regulation
  private lateinit var regulationNotActive: Regulation
  private lateinit var regulationOperationDeleted: Regulation

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    group = GroupSeeder().seedGroup(user.userId, groupStatus = GroupStatus.PRIVATE_ACTIVE)
    regulationActive = RegulationSeeder().seedRegulation(
      group.groupId,
      user.userId,
      RegulationStatus.ACTIVE
    )
    regulationNotActive = RegulationSeeder().seedRegulation(
      group.groupId,
      user.userId,
      RegulationStatus.NOT_ACTIVE
    )
    regulationOperationDeleted = RegulationSeeder().seedRegulation(
      group.groupId,
      user.userId,
      RegulationStatus.OPERATION_DELETED
    )
  }

  @Test
  fun testGetDeletedByUserTwo() = runTest {
    val publicGroup = GroupSeeder().seedGroup(user.userId, groupStatus = GroupStatus.PUBLIC_ACTIVE)
    RegulationSeeder().seedRegulation(
      group.groupId,
      user.userId,
      RegulationStatus.OPERATION_DELETED
    )
    Assertions.assertEquals(
      listOf<Regulation>(),
      getRegulationAdmin.getRegulationsByGroupId(
        publicGroup.groupId,
        RegulationStatus.values().toList()
      )
    )
  }

  @Test
  fun testGetRegulationsByGroupIdA() = runTest {
    val regulations = getRegulationAdmin.getRegulationsByGroupId(
      group.groupId,
      RegulationStatus.values().toList()
    )
    Assertions.assertEquals(
      listOf(regulationActive, regulationNotActive, regulationOperationDeleted),
      regulations
    )
  }

  @Test
  fun testGetRegulationsByGroupIdB() = runTest {
    val regulations = getRegulationAdmin.getRegulationsByGroupId(
      group.groupId,
      listOf(RegulationStatus.ACTIVE)
    )
    Assertions.assertEquals(
      listOf(regulationActive),
      regulations
    )
  }

  @Test
  fun testGetRegulationsByGroupIdWithNoPermission() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        getRegulationAdmin.getRegulationsByGroupId(
          group.groupId,
          RegulationStatus.values().toList()
        )
      }
    }
  }
}
