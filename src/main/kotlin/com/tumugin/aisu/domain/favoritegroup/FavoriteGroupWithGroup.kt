package com.tumugin.aisu.domain.favoritegroup

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId

class FavoriteGroupWithGroup(
  favoriteGroupId: FavoriteGroupId,
  userId: UserId,
  groupId: GroupId,
  val group: Group?
) : FavoriteGroup(favoriteGroupId, userId, groupId) {
}
