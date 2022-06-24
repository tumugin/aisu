package com.tumugin.aisu.domain.cheki

import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class Cheki(
  val chekiId: ChekiId,
  val userId: UserId,
  val user: User,
  val idolId: IdolId?,
  val idol: Idol?,
  val regulationId: RegulationId?,
  val regulation: Regulation?,
  val chekiQuantity: ChekiQuantity,
  val chekiShotAt: ChekiShotAt,
  val chekiCreatedAt: ChekiCreatedAt,
  val chekiUpdatedAt: ChekiUpdatedAt
) {
  fun isVisibleToUser(userId: UserId?): Boolean {
    return this.userId == userId
  }

  fun isEditableByUser(userId: UserId?): Boolean {
    return this.userId == userId
  }
}
