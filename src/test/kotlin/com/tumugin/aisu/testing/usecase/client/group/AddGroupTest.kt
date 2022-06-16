package com.tumugin.aisu.testing.usecase.client.group

import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.group.GetGroup
import com.tumugin.aisu.usecase.client.group.WriteGroup
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddGroupTest : BaseDatabaseTest() {
  private val writeGroup = WriteGroup()
  private val getGroup = GetGroup()
  private lateinit var userOne: User

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
  }

  @Test
  fun testAddGroup() = runTest {
    val addedGroup = writeGroup.addGroup(
      userOne.userId, GroupName("Appare!"), GroupStatus.PUBLIC_ACTIVE
    )
    val actualGroup = getGroup.getGroup(userOne.userId, addedGroup.groupId)
    assertEquals(addedGroup.groupName, actualGroup?.groupName)
    assertEquals(addedGroup.groupStatus, actualGroup?.groupStatus)
    assertEquals(addedGroup.groupId, actualGroup?.groupId)
    assertEquals(addedGroup.user, actualGroup?.user)
    assertEquals(addedGroup.userId, actualGroup?.userId)
  }
}
