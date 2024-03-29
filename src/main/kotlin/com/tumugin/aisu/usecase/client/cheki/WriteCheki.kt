package com.tumugin.aisu.usecase.client.cheki

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.InvalidContextException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.client.idol.GetIdol
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteCheki : KoinComponent {
  private val getCheki = GetCheki()
  private val getIdol = GetIdol()
  private val getRegulation = GetRegulation()
  private val chekiRepository by inject<ChekiRepository>()

  suspend fun addCheki(
    sessionUserId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt,
  ): Cheki {
    val idol = getIdol.getIdol(sessionUserId, idolId) ?: throw InvalidContextException()
    if (!idol.isVisibleToUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    if (regulationId != null) {
      val regulation = getRegulation.getRegulation(sessionUserId, regulationId) ?: throw InvalidContextException()
      if (!regulation.isVisibleToUser(sessionUserId)) {
        throw HasNoPermissionException()
      }
    }

    return chekiRepository.addCheki(
      userId = sessionUserId,
      idolId = idolId,
      regulationId = regulationId,
      chekiQuantity = chekiQuantity,
      chekiShotAt = chekiShotAt,
    )
  }

  suspend fun updateCheki(
    chekiId: ChekiId,
    sessionUserId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt,
  ): Cheki {
    val cheki = getCheki.getCheki(sessionUserId, chekiId) ?: throw NotFoundException()
    if (!cheki.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    val idol = getIdol.getIdol(sessionUserId, idolId) ?: throw InvalidContextException()
    if (!idol.isVisibleToUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    if (regulationId != null) {
      val regulation = getRegulation.getRegulation(sessionUserId, regulationId) ?: throw InvalidContextException()
      if (!regulation.isVisibleToUser(sessionUserId)) {
        throw HasNoPermissionException()
      }
    }

    return chekiRepository.updateCheki(
      chekiId = chekiId,
      userId = cheki.userId,
      idolId = idolId,
      regulationId = regulationId,
      chekiQuantity = chekiQuantity,
      chekiShotAt = chekiShotAt,
    )
  }

  suspend fun deleteCheki(sessionUserId: UserId, chekiId: ChekiId) {
    val cheki = getCheki.getCheki(sessionUserId, chekiId) ?: throw NotFoundException()
    if (!cheki.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }

    chekiRepository.deleteCheki(chekiId)
  }
}
