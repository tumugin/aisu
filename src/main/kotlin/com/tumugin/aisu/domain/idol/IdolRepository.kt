package com.tumugin.aisu.domain.idol

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId

interface IdolRepository {
  suspend fun getIdol(idolId: IdolId): Idol?

  suspend fun addIdol(
    groupId: GroupId,
    userId: UserId,
    idolName: IdolName,
    idolStatus: IdolStatus,
  ): Idol

  suspend fun updateIdol(
    idolId: IdolId,
    groupId: GroupId,
    userId: UserId,
    idolName: IdolName,
    idolStatus: IdolStatus,
  ): Idol

  suspend fun deleteIdol(idolId: IdolId)
}
