package com.tumugin.aisu.domain.cheki

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId

data class ChekiIdolGroupCount(
  val idol: Idol?,
  val idolId: IdolId,
  val groupId: GroupId?,
  val group: Group?,
  val chekiCount: ChekiCount,
  val totalPrice: TotalPriceOfCheki
)
