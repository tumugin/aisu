package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import kotlin.test.*

class GroupRepositoryTest : BaseDatabaseTest() {
  private val groupRepository by inject<GroupRepository>()

  @BeforeTest
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    GroupSeeder().seedGroup(user.userId)
  }

  @Test
  fun testGetGroup() = runTest {
    val group = groupRepository.getGroup(GroupId(1))!!
    assertNotNull(group.user)
    assertEquals(
      mapOf(
        "groupId" to 1L,
        "userId" to 1L,
        "groupName" to "群青の世界",
        "groupStatus" to GroupStatus.PRIVATE_ACTIVE
      ),
      mapOf(
        "groupId" to group.groupId.value,
        "userId" to group.userId?.value,
        "groupName" to group.groupName.value,
        "groupStatus" to group.groupStatus
      )
    )
  }

  @Test
  fun testAddGroup() = runTest {
    val group = groupRepository.addGroup(
      UserId(1),
      GroupName("Appare!"),
      GroupStatus.PRIVATE_ACTIVE
    )
    val addedGroup = groupRepository.getGroup(group.groupId)!!
    assertEquals(
      mapOf(
        "groupId" to group.groupId.value,
        "userId" to 1L,
        "groupName" to "Appare!",
        "groupStatus" to GroupStatus.PRIVATE_ACTIVE
      ),
      mapOf(
        "groupId" to addedGroup.groupId.value,
        "userId" to addedGroup.userId?.value,
        "groupName" to addedGroup.groupName.value,
        "groupStatus" to addedGroup.groupStatus
      )
    )
  }

  @Test
  fun testUpdateGroup() = runTest {
    groupRepository.updateGroup(
      GroupId(1),
      UserId(1),
      GroupName("FES☆TIVE"),
      GroupStatus.PUBLIC_ACTIVE
    )
    val updatedGroup = groupRepository.getGroup(GroupId(1))!!
    assertEquals(
      mapOf(
        "groupId" to 1L,
        "userId" to 1L,
        "groupName" to "FES☆TIVE",
        "groupStatus" to GroupStatus.PUBLIC_ACTIVE
      ),
      mapOf(
        "groupId" to updatedGroup.groupId.value,
        "userId" to updatedGroup.userId?.value,
        "groupName" to updatedGroup.groupName.value,
        "groupStatus" to updatedGroup.groupStatus
      )
    )
  }

  @Test
  fun testDeleteGroup() = runTest {
    groupRepository.deleteGroup(GroupId(1))
    assertNull(groupRepository.getGroup(GroupId(1)))
  }
}
