package com.tumugin.aisu.usecase.admin.favoritegroup

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupWithGroup
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetFavoriteGroupAdmin : KoinComponent {
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()
  private val groupRepository by inject<GroupRepository>()

  suspend fun getFavoriteGroupsByUserId(sessionUserId: UserId): List<FavoriteGroupWithGroup> {
    val favoriteGroups = favoriteGroupRepository.getFavoriteGroupsByUserId(sessionUserId)
    val groupIds = favoriteGroups.map { it.groupId }
    val groups = groupRepository.getGroups(groupIds).filter { it.isVisibleToUser(sessionUserId) }
    return favoriteGroups.map { favoriteGroup ->
      FavoriteGroupWithGroup(
        favoriteGroup.favoriteGroupId,
        favoriteGroup.userId,
        favoriteGroup.groupId,
        groups.find { it.groupId == favoriteGroup.groupId })
    }
  }
}
