package com.tumugin.aisu.usecase.client.idol

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.idol.IdolRepository
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetIdol : KoinComponent {
  private val idolRepository by inject<IdolRepository>()

  suspend fun getIdol(sessionUserId: UserId?, idolId: IdolId): Idol? {
    val idol = idolRepository.getIdol(idolId) ?: return null
    if (idol.isVisibleToUser(sessionUserId)) {
      return idol
    }
    throw HasNoPermissionException()
  }

  suspend fun getIdolsByIds(sessionUserId: UserId?, idolIds: List<IdolId>): List<Idol> {
    val idols = idolRepository.getIdolsByIds(idolIds)
    return idols.filter { it.isVisibleToUser(sessionUserId) }
  }

  suspend fun getAllPublicIdols(paginatorParam: PaginatorParam): PaginatorResult<Idol> {
    return idolRepository.getAllIdolsByStatues(
      paginatorParam, IdolStatus.allPublicStatuses, null
    )
  }

  suspend fun getAllUserCreatedIdols(sessionUserId: UserId, paginatorParam: PaginatorParam): PaginatorResult<Idol> {
    return idolRepository.getAllIdolsByStatues(
      paginatorParam, IdolStatus.allStatues, sessionUserId
    )
  }

  suspend fun getGroupsOfIdol(sessionUserId: UserId?, idolId: IdolId): List<Group> {
    val groups = idolRepository.getGroupsOfIdol(idolId)
    return groups.filter { it.isVisibleToUser(sessionUserId) }
  }

  suspend fun getGroupIdsOfIdols(sessionUserId: UserId?, idolIds: List<IdolId>): Map<IdolId, List<GroupId>> {
    val idols = idolRepository.getIdolsByIds(idolIds)
    val filteredIdols = idols.filter { idol -> idol.isVisibleToUser(sessionUserId) }
    return idolRepository.getGroupIdsOfIdols(filteredIdols.map { it.idolId })
  }
}
