package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class Group(
  val groupId: GroupId,
  val userId: UserId?,
  val user: User?,
  val groupName: GroupName,
  val groupStatus: GroupStatus,
  val groupCreatedAt: GroupCreatedAt,
  val groupUpdatedAt: GroupUpdatedAt
) {
  private val ownGroupsVisibleStatuses = listOf(
    GroupStatus.PRIVATE_ACTIVE,
    GroupStatus.PRIVATE_NOT_ACTIVE,
    GroupStatus.PUBLIC_ACTIVE,
    GroupStatus.PUBLIC_NOT_ACTIVE,
  )
  private val notOwnGroupsVisibleStatuses = listOf(
    GroupStatus.PUBLIC_ACTIVE,
    GroupStatus.PUBLIC_NOT_ACTIVE,
  )

  fun isVisibleToUser(userId: UserId?): Boolean {
    // 自分自身が持っているグループの場合
    if (this.user?.userId == userId && ownGroupsVisibleStatuses.contains(this.groupStatus)) {
      return true
    }

    // 他人が作成したグループの場合
    if (notOwnGroupsVisibleStatuses.contains(this.groupStatus)) {
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
