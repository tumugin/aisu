package com.tumugin.aisu.usecase.admin.cheki

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.InvalidContextException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.admin.idol.GetIdolAdmin
import com.tumugin.aisu.usecase.admin.regulation.GetRegulationAdmin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteChekiAdmin : KoinComponent {
  private val getCheki = GetChekiAdmin()
  private val getIdol = GetIdolAdmin()
  private val getRegulation = GetRegulationAdmin()
  private val chekiRepository by inject<ChekiRepository>()

  suspend fun addCheki(
    clientUserId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt,
  ): Cheki {
    val idol = getIdol.getIdol(idolId) ?: throw InvalidContextException()
    if (!idol.isVisibleToAdmin()) {
      throw HasNoPermissionException()
    }
    if (regulationId != null) {
      val regulation = getRegulation.getRegulation(regulationId) ?: throw InvalidContextException()
      if (!regulation.isVisibleToAdmin()) {
        throw HasNoPermissionException()
      }
    }

    return chekiRepository.addCheki(
      userId = clientUserId,
      idolId = idolId,
      regulationId = regulationId,
      chekiQuantity = chekiQuantity,
      chekiShotAt = chekiShotAt,
    )
  }

  suspend fun updateCheki(
    chekiId: ChekiId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt,
  ): Cheki {
    val cheki = getCheki.getCheki(chekiId) ?: throw NotFoundException()
    if (!cheki.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    val idol = getIdol.getIdol(idolId) ?: throw InvalidContextException()
    if (!idol.isVisibleToAdmin()) {
      throw HasNoPermissionException()
    }
    if (regulationId != null) {
      val regulation = getRegulation.getRegulation(regulationId) ?: throw InvalidContextException()
      if (!regulation.isVisibleToAdmin()) {
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

  suspend fun deleteCheki(chekiId: ChekiId) {
    val cheki = getCheki.getCheki(chekiId) ?: throw NotFoundException()
    if (!cheki.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }

    chekiRepository.deleteCheki(chekiId)
  }
}
