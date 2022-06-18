package com.tumugin.aisu.testing.usecase.client.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
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

class AddIdolToGroupTest : BaseDatabaseTest() {
  private val getGroup = GetGroup()
  private val writeGroup = WriteGroup()
  private lateinit var userOne: User
  private lateinit var userTwo: User
  private lateinit var targetGroup: Group
  private lateinit var targetIdol: Idol

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    targetGroup = GroupSeeder().seedGroup(userOne.userId)
    targetIdol = IdolSeeder().seedIdol(userOne.userId)
  }

  @Test
  fun testAddIdolToGroup() = runTest {
    writeGroup.addIdolToGroup(
      userOne.userId,
      targetGroup,
      targetIdol
    )
    val idols = getGroup.getIdolsOfGroup(userOne.userId, targetGroup)
    assertEquals(
      listOf(targetIdol),
      idols
    )
  }

  @Test
  fun testNotAddableIdolToGroup() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeGroup.addIdolToGroup(
          userTwo.userId,
          targetGroup,
          targetIdol
        )
      }
    }
  }
}
