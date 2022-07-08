package com.tumugin.aisu.testing.usecase.admin.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.group.GetGroupAdmin
import com.tumugin.aisu.usecase.admin.group.WriteGroupAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateGroupTest : BaseDatabaseTest() {
  private val getGroupAdmin = GetGroupAdmin()
  private val writeGroupAdmin = WriteGroupAdmin()
  private lateinit var userOne: User
  private lateinit var userTwo: User
  private lateinit var targetGroup: Group

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    targetGroup = GroupSeeder().seedGroup(
      userOne.userId,
      GroupName("アンス"),
      GroupStatus.PRIVATE_ACTIVE
    )
  }

  @Test
  fun testUpdateGroup() = runTest {
    val updatedGroup = writeGroupAdmin.updateGroup(
      targetGroup,
      GroupName("アンスリューム"),
      GroupStatus.PUBLIC_ACTIVE
    )
    val dbUpdatedGroup = getGroupAdmin.getGroup(
      updatedGroup.groupId
    )!!
    Assertions.assertEquals(updatedGroup.groupName, dbUpdatedGroup.groupName)
    Assertions.assertEquals(updatedGroup.groupStatus, dbUpdatedGroup.groupStatus)
  }

  @Test
  fun testNotUpdatableGroup() = runTest {
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeGroupAdmin.updateGroup(
          targetGroup,
          GroupName("アンスリューム"),
          GroupStatus.PUBLIC_ACTIVE
        )
      }
    }
  }
}
