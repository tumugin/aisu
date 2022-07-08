package com.tumugin.aisu.testing.usecase.admin.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
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
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class DeleteGroupTest : BaseDatabaseTest() {
  private val getGroupAdmin = GetGroupAdmin()
  private val writeGroupAdmin = WriteGroupAdmin()
  private lateinit var userOne: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE", "PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE", "OPERATION_DELETED"])
  fun testDeletableGroup(groupStatus: String) = runTest {
    val group = GroupSeeder().seedGroup(userOne.userId, groupStatus = GroupStatus.valueOf(groupStatus))
    writeGroupAdmin.deleteGroup(userOne.userId, group)
    assertNull(getGroupAdmin.getGroup(group.groupId))
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE", "PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE", "OPERATION_DELETED"])
  fun testNotDeletableGroup(groupStatus: String) = runTest {
    val group = GroupSeeder().seedGroup(userTwo.userId, groupStatus = GroupStatus.valueOf(groupStatus))
    Assertions.assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        writeGroupAdmin.deleteGroup(userOne.userId, group)
      }
    }
  }
}
