package com.tumugin.aisu.domain.idol

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId

data class Idol(
  val idolId: IdolId,
  val groupId: GroupId?,
  val userId: UserId?,
  val idolName: IdolName,
  val idolStatus: IdolStatus,
  val idolCreatedAt: IdolCreatedAt,
  val idolUpdatedAt: IdolUpdatedAt
) {}
