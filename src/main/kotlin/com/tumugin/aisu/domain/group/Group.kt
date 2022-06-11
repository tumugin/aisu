package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class Group(
  val groupId: GroupId,
  val userId: UserId?,
  val user: User?,
  val idols: List<Idol>,
  val groupName: GroupName,
  val groupStatus: GroupStatus,
  val groupCreatedAt: GroupCreatedAt,
  val groupUpdatedAt: GroupUpdatedAt
) {}
