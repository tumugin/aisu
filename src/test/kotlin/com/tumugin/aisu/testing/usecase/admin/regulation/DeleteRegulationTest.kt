package com.tumugin.aisu.testing.usecase.admin.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.regulation.GetRegulationAdmin
import com.tumugin.aisu.usecase.admin.regulation.WriteRegulationAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DeleteRegulationTest : BaseDatabaseTest() {
  private val writeRegulationAdmin = WriteRegulationAdmin()
  private val getRegulationAdmin = GetRegulationAdmin()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var group: Group
  private lateinit var regulation: Regulation

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    group = GroupSeeder().seedGroup(user.userId)
    regulation = RegulationSeeder().seedRegulation(group.groupId, user.userId)
  }

  @Test
  fun testDeleteRegulation() = runTest {
    writeRegulationAdmin.deleteRegulation(regulation.regulationId)
    Assertions.assertNull(getRegulationAdmin.getRegulation(regulation.regulationId))
  }

  @Test
  fun testDeleteRegulationWithNoPermission() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeRegulationAdmin.deleteRegulation(regulation.regulationId)
      }
    }
  }
}
