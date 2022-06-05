package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroup
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FavoriteGroupSeeder : KoinComponent {
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()

  suspend fun seedFavoriteGroup(
    userId: UserId,
    groupId: GroupId
  ): FavoriteGroup {
    return favoriteGroupRepository.addFavoriteGroup(userId, groupId)
  }
}
