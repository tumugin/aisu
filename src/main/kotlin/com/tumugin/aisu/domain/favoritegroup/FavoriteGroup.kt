package com.tumugin.aisu.domain.favoritegroup

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class FavoriteGroup(
  val favoriteGroupId: FavoriteGroupId,
  val userId: UserId,
  val user: User,
  val groupId: GroupId,
  val group: Group
) {}
