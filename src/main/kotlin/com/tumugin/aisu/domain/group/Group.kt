package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.user.UserId

data class Group(
  val groupId: GroupId,
  val userId: UserId?,
  val groupName: GroupName,
  val groupStatus: GroupStatus,
  val groupCreatedAt: GroupCreatedAt,
  val groupUpdatedAt: GroupUpdatedAt
) {}
