package com.tumugin.aisu.domain.idol

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class Idol(
  val idolId: IdolId,
  val groupId: GroupId?,
  val group: Group?,
  val userId: UserId?,
  val user: User?,
  val idolName: IdolName,
  val idolStatus: IdolStatus,
  val idolCreatedAt: IdolCreatedAt,
  val idolUpdatedAt: IdolUpdatedAt
) {}
