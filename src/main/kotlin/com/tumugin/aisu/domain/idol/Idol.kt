package com.tumugin.aisu.domain.idol

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class Idol(
  val idolId: IdolId,
  val userId: UserId?,
  val idolName: IdolName,
  val idolStatus: IdolStatus,
  val idolCreatedAt: IdolCreatedAt,
  val idolUpdatedAt: IdolUpdatedAt
) {
  private val ownIdolVisibleStatues = listOf(
    IdolStatus.PRIVATE_ACTIVE,
    IdolStatus.PRIVATE_NOT_ACTIVE,
    IdolStatus.PUBLIC_ACTIVE,
    IdolStatus.PUBLIC_NOT_ACTIVE,
    IdolStatus.OPERATION_DELETED,
  )
  private val notOwnIdolVisibleStatues = listOf(
    IdolStatus.PUBLIC_ACTIVE,
    IdolStatus.PUBLIC_NOT_ACTIVE,
  )

  fun isVisibleToUser(userId: UserId?): Boolean {
    // 自分自身が持っているグループの場合
    if (this.userId == userId && ownIdolVisibleStatues.contains(this.idolStatus)) {
      return true
    }

    // 他人が作成したグループの場合
    if (notOwnIdolVisibleStatues.contains(this.idolStatus)) {
      return true
    }

    return false
  }

  fun isEditableByUser(userId: UserId?): Boolean {
    if (this.userId == userId) {
      return true
    }
    return false
  }
}
