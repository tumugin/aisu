package com.tumugin.aisu.domain.cheki

import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId

data class Cheki(
  val chekiId: ChekiId,
  val userId: UserId,
  val idolId: IdolId,
  val regulationId: RegulationId,
  val chekiQuantity: ChekiQuantity,
  val chekiShotAt: ChekiShotAt,
  val chekiCreatedAt: ChekiCreatedAt,
  val chekiUpdatedAt: ChekiUpdatedAt
) {}
