package com.tumugin.aisu.testing.usecase.client.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetRegulationTest : BaseDatabaseTest() {
  private val getRegulation = GetRegulation()
  private lateinit var user: User
  private lateinit var userTwo: User
  private lateinit var regulation: Regulation

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    regulation = RegulationSeeder().seedRegulation(
      GroupSeeder().seedGroup(user.userId, groupStatus = GroupStatus.PRIVATE_ACTIVE).groupId,
      user.userId
    )
  }

  @Test
  fun getRegulationTest() = runTest {
    val retrievedRegulation = getRegulation.getRegulation(user.userId, regulation.regulationId)
    Assertions.assertEquals(
      regulation,
      retrievedRegulation
    )
  }

  @Test
  fun getRegulationTestWithNoPermission() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        getRegulation.getRegulation(userTwo.userId, regulation.regulationId)
      }
    }
  }
}
