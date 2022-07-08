package com.tumugin.aisu.testing.usecase.admin.group

import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.group.GetGroupAdmin
import com.tumugin.aisu.usecase.admin.group.WriteGroupAdmin
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AddGroupTest : BaseDatabaseTest() {
  private val writeGroupAdmin = WriteGroupAdmin()
  private val getGroupAdmin = GetGroupAdmin()
  private lateinit var userOne: User

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
  }

  @Test
  fun testAddGroup() = runTest {
    val addedGroup = writeGroupAdmin.addGroup(
      userOne.userId, GroupName("Appare!"), GroupStatus.PUBLIC_ACTIVE
    )
    val actualGroup = getGroupAdmin.getGroup(addedGroup.groupId)
    assertEquals(addedGroup.groupName, actualGroup?.groupName)
    assertEquals(addedGroup.groupStatus, actualGroup?.groupStatus)
    assertEquals(addedGroup.groupId, actualGroup?.groupId)
    assertEquals(addedGroup.userId, actualGroup?.userId)
  }
}
