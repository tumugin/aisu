package com.tumugin.aisu.testing.usecase.client.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.RegulationSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import com.tumugin.aisu.usecase.client.regulation.WriteRegulation
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateRegulationTest : BaseDatabaseTest() {
  private val writeRegulation = WriteRegulation()
  private val getRegulation = GetRegulation()
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
  fun testUpdateRegulation() = runTest {
    val regulation = writeRegulation.updateRegulation(
      regulation.regulationId,
      user.userId,
      RegulationName("test"),
      RegulationComment("test"),
      RegulationUnitPrice(100),
      RegulationStatus.NOT_ACTIVE
    )
    val fetchedRegulation = getRegulation.getRegulation(user.userId, regulation.regulationId)

    Assertions.assertEquals(regulation.regulationId, fetchedRegulation?.regulationId)
    Assertions.assertEquals(regulation.groupId, fetchedRegulation?.groupId)
    Assertions.assertEquals(regulation.userId, fetchedRegulation?.userId)
    Assertions.assertEquals(regulation.regulationName, fetchedRegulation?.regulationName)
    Assertions.assertEquals(regulation.regulationComment, fetchedRegulation?.regulationComment)
    Assertions.assertEquals(regulation.regulationUnitPrice, fetchedRegulation?.regulationUnitPrice)
    Assertions.assertEquals(regulation.regulationStatus, fetchedRegulation?.regulationStatus)
  }

  @Test
  fun testUpdateRegulationWithNoPermission() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeRegulation.updateRegulation(
          regulation.regulationId,
          userTwo.userId,
          RegulationName("test"),
          RegulationComment("test"),
          RegulationUnitPrice(100),
          RegulationStatus.NOT_ACTIVE
        )
      }
    }
  }
}
