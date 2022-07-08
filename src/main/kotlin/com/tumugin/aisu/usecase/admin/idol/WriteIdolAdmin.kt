package com.tumugin.aisu.usecase.admin.idol

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteIdolAdmin : KoinComponent {
  private val idolRepository by inject<IdolRepository>()

  suspend fun addIdol(
    clientUserId: UserId,
    idolName: IdolName,
    idolStatus: IdolStatus,
  ): Idol {
    return idolRepository.addIdol(
      clientUserId, idolName, idolStatus
    )
  }

  suspend fun updateIdol(
    idolId: IdolId,
    clientUserId: UserId,
    idolName: IdolName,
    idolStatus: IdolStatus,
  ): Idol {
    val idol = idolRepository.getIdol(idolId) ?: throw NotFoundException()
    if (!idol.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    return idolRepository.updateIdol(
      idolId, clientUserId, idolName, idolStatus
    )
  }

  suspend fun deleteIdol(idolId: IdolId) {
    val idol = idolRepository.getIdol(idolId) ?: throw NotFoundException()
    if (!idol.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    idolRepository.deleteIdol(idolId)
  }
}
