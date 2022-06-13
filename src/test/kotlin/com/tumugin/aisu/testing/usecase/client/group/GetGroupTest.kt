package com.tumugin.aisu.testing.usecase.client.group

import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.group.GetGroup
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.test.assertEquals

class GetGroupTest : BaseDatabaseTest() {
  private val getGroup = GetGroup()
  lateinit var userOne: User
  lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["public_active", "public_not_active"])
  fun testGetPublicGroup(status: String) = runTest {
    val createdGroup = GroupSeeder().seedGroup(userOne.userId, groupStatus = GroupStatus.valueOf(status))
    val retrievedGroup = getGroup.getGroup(userTwo.userId, createdGroup.groupId)
    assertEquals(createdGroup, retrievedGroup)
  }
}
