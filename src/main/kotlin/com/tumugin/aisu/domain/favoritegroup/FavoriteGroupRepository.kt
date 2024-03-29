package com.tumugin.aisu.domain.favoritegroup

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId

interface FavoriteGroupRepository {
  suspend fun getFavoriteGroupsByUserId(userId: UserId): List<FavoriteGroup>
  suspend fun getFavoriteGroup(favoriteGroupId: FavoriteGroupId): FavoriteGroup?
  suspend fun deleteFavoriteGroup(favoriteGroupId: FavoriteGroupId)
  suspend fun addFavoriteGroup(
    userId: UserId,
    groupId: GroupId
  ): FavoriteGroup
}
