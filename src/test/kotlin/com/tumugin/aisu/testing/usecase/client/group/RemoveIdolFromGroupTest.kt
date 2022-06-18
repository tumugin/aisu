package com.tumugin.aisu.testing.usecase.client.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupIdolSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.group.GetGroup
import com.tumugin.aisu.usecase.client.group.WriteGroup
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RemoveIdolFromGroupTest : BaseDatabaseTest() {
  private val getGroup = GetGroup()
  private val writeGroup = WriteGroup()
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
    GroupIdolSeeder().seedGroupIdol(targetGroup.groupId, targetIdol.idolId)
    GroupIdolSeeder().seedGroupIdol(targetGroupTwo.groupId, targetIdolTwo.idolId)
  }

  @Test
  fun testRemoveIdolFromGroup() = runTest {
    writeGroup.removeIdolFromGroup(
      userOne.userId,
      targetGroup,
      targetIdol
    )
    val idols = getGroup.getIdolsOfGroup(userOne.userId, targetGroup)
    assertEquals(
      listOf(),
      idols
    )
  }

  @Test
  fun testNotAddableIdolToGroupPatternA() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeGroup.removeIdolFromGroup(
          userTwo.userId,
          targetGroup,
          targetIdol
        )
      }
    }
  }

  @Test
  fun testNotAddableIdolToGroupPatternB() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeGroup.removeIdolFromGroup(
          userTwo.userId,
          targetGroupTwo,
          targetIdol
        )
      }
    }
  }

  @Test
  fun testNotAddableIdolToGroupPatternC() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeGroup.removeIdolFromGroup(
          userTwo.userId,
          targetGroup,
          targetIdolTwo
        )
      }
    }
  }
}
