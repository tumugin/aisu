package com.tumugin.aisu.domain.idol

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
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

  suspend fun getAllIdolsByStatues(
    paginatorParam: PaginatorParam, statues: List<IdolStatus>
  ): PaginatorResult<Idol>
}
