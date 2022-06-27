package com.tumugin.aisu.testing.usecase.client.regulation

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.regulation.RegulationComment
import com.tumugin.aisu.domain.regulation.RegulationName
import com.tumugin.aisu.domain.regulation.RegulationStatus
import com.tumugin.aisu.domain.regulation.RegulationUnitPrice
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import com.tumugin.aisu.usecase.client.regulation.WriteRegulation
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddRegulationTest : BaseDatabaseTest() {
  private val writeRegulation = WriteRegulation()
  private val getRegulation = GetRegulation()
  private lateinit var user: User
  private lateinit var group: Group

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    group = GroupSeeder().seedGroup(user.userId)
  }

  @Test
  fun testAddRegulation() = runTest {
    val regulation = writeRegulation.addRegulation(
      group.groupId,
      user.userId,
      RegulationName("test regulation"),
      RegulationComment("test regulation comment"),
      RegulationUnitPrice(100),
      RegulationStatus.ACTIVE
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
}
