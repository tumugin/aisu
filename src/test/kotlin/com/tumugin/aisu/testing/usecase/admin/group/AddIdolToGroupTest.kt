package com.tumugin.aisu.testing.usecase.admin.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.group.GetGroupAdmin
import com.tumugin.aisu.usecase.admin.group.WriteGroupAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddIdolToGroupTest : BaseDatabaseTest() {
  private val getGroupAdmin = GetGroupAdmin()
  private val writeGroupAdmin = WriteGroupAdmin()
  private lateinit var userOne: User
  private lateinit var userTwo: User
  private lateinit var targetGroup: Group
  private lateinit var targetGroupTwo: Group
  private lateinit var targetIdol: Idol
  private lateinit var targetIdolTwo: Idol

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    targetGroup = GroupSeeder().seedGroup(userOne.userId)
    targetGroupTwo = GroupSeeder().seedGroup(userTwo.userId)
    targetIdol = IdolSeeder().seedIdol(userOne.userId)
    targetIdolTwo = IdolSeeder().seedIdol(userTwo.userId)
  }

  @Test
  fun testAddIdolToGroup() = runTest {
    writeGroupAdmin.addIdolToGroup(
      targetGroup,
      targetIdol
    )
    val idols = getGroupAdmin.getIdolsOfGroup(targetGroup)
    assertEquals(
      listOf(targetIdol),
      idols
    )
  }

  @Test
  fun testNotAddableIdolToGroupPatternA() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeGroupAdmin.addIdolToGroup(
          targetGroup,
          targetIdol
        )
      }
    }
  }

  @Test
  fun testNotAddableIdolToGroupPatternB() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeGroupAdmin.addIdolToGroup(
          targetGroupTwo,
          targetIdol
        )
      }
    }
  }

  @Test
  fun testNotAddableIdolToGroupPatternC() = runTest {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeGroupAdmin.addIdolToGroup(
          targetGroup,
          targetIdolTwo
        )
      }
    }
  }
}
