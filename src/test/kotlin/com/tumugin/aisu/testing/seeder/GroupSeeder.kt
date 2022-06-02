package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GroupSeeder : KoinComponent {
  private val groupRepository by inject<GroupRepository>()

  suspend fun seedGroup(userId: UserId): Group {
    return groupRepository.addGroup(
      userId,
      GroupName("群青の世界"),
      GroupStatus.PRIVATE_ACTIVE
    )
  }
}
