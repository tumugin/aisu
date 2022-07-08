package com.tumugin.aisu.usecase.admin.favoritegroup

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroup
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteFavoriteGroupAdmin : KoinComponent {
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()

  suspend fun deleteFavoriteGroup(sessionUserId: UserId, favoriteGroupId: FavoriteGroupId) {
    val favoriteGroup = favoriteGroupRepository.getFavoriteGroup(favoriteGroupId) ?: throw NotFoundException()
    if (!favoriteGroup.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    favoriteGroupRepository.deleteFavoriteGroup(favoriteGroupId)
  }

  suspend fun addFavoriteGroup(
    sessionUserId: UserId,
    groupId: GroupId
  ): FavoriteGroup {
    return favoriteGroupRepository.addFavoriteGroup(sessionUserId, groupId)
  }
}
