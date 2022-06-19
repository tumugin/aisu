package com.tumugin.aisu.testing.usecase.client.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.group.GetGroup
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetGroupTest : BaseDatabaseTest() {
  private val getGroup = GetGroup()
  private lateinit var userOne: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE"])
  fun testGetPublicGroup(status: String) = runTest {
    val createdGroup = GroupSeeder().seedGroup(userOne.userId, groupStatus = GroupStatus.valueOf(status))
    val retrievedGroup = getGroup.getGroup(userTwo.userId, createdGroup.groupId)
    assertEquals(createdGroup, retrievedGroup)
  }

  @ParameterizedTest
  @ValueSource(strings = ["PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE", "OPERATION_DELETED"])
  fun testGetNotRetrievableGroup(status: String) = runTest {
    val createdGroup = GroupSeeder().seedGroup(userOne.userId, groupStatus = GroupStatus.valueOf(status))

    val retrievedGroup = getGroup.getGroup(userOne.userId, createdGroup.groupId)
    assertEquals(createdGroup, retrievedGroup)

    assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        getGroup.getGroup(userTwo.userId, createdGroup.groupId)
      }
    }
  }

  @Test
  fun testGetOperationDeletedGroup() = runTest {
    val createdGroup = GroupSeeder().seedGroup(userOne.userId, groupStatus = GroupStatus.OPERATION_DELETED)

    // 運営削除されたものは自分が作った場合のみ取得出来る
    val retrievedGroup = getGroup.getGroup(userOne.userId, createdGroup.groupId)
    assertNotNull(retrievedGroup)

    // 他人からは取得出来ない
    assertThrows(HasNoPermissionException::class.java) {
      runBlocking {
        getGroup.getGroup(userTwo.userId, createdGroup.groupId)
      }
    }
  }
}
