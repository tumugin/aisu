package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.user.UserId

data class Group(
  val groupId: GroupId,
  val userId: UserId?,
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
    GroupStatus.OPERATION_DELETED,
  )
  private val notOwnGroupsVisibleStatuses = listOf(
    GroupStatus.PUBLIC_ACTIVE,
    GroupStatus.PUBLIC_NOT_ACTIVE,
  )

  fun isVisibleToUser(userId: UserId?): Boolean {
    // 自分自身が持っているグループの場合
    if (this.userId == userId && ownGroupsVisibleStatuses.contains(this.groupStatus)) {
      return true
    }

    // 他人が作成したグループの場合
    if (notOwnGroupsVisibleStatuses.contains(this.groupStatus)) {
      return true
    }

    return false
  }

  fun isVisibleToAdmin(): Boolean {
    return true
  }

  fun isEditableByUser(userId: UserId?): Boolean {
    if (this.userId == userId) {
      return true
    }
    return false
  }

  fun isEditableByAdmin(): Boolean {
    return true
  }
}
