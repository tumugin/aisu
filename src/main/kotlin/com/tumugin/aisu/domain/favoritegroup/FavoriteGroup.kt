package com.tumugin.aisu.domain.favoritegroup

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId

open class FavoriteGroup(
  val favoriteGroupId: FavoriteGroupId,
  val userId: UserId,
  val groupId: GroupId,
) {
  fun isVisibleByUser(userId: UserId): Boolean {
    return this.userId == userId
  }

  fun isEditableByUser(userId: UserId): Boolean {
    return this.userId == userId
  }

  fun isEditableByAdmin(): Boolean {
    return true
  }
}
