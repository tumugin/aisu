package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.idol.IdolId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GroupIdolSeeder : KoinComponent {
  private val groupRepository by inject<GroupRepository>()

  suspend fun seedGroupIdol(groupId: GroupId, idolId: IdolId) {
    groupRepository.addIdolToGroup(groupId, idolId)
  }
}
