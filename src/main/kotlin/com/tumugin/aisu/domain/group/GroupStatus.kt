package com.tumugin.aisu.domain.group

enum class GroupStatus(s: String) {
  /**
   * 公開状態かつ有効(活動中)
   */
  PUBLIC_ACTIVE("public_active"),

  /**
   * 公開状態かつ有効でない(活動休止、解散など)
   */
  PUBLIC_NOT_ACTIVE("public_not_active"),

  /**
   * 非公開状態(作ったユーザのみが閲覧可能)かつ有効(活動中)
   */
  PRIVATE_ACTIVE("private_active"),

  /**
   * 非公開状態(作ったユーザのみが閲覧可能)かつ有効でない(活動休止、解散など)
   */
  PRIVATE_NOT_ACTIVE("private_not_active"),

  /**
   * 運営削除(論理削除)
   */
  OPERATION_DELETED("operation_deleted");

  companion object {
    val allUserCanSpecifyStatus = listOf(PUBLIC_ACTIVE, PUBLIC_NOT_ACTIVE, PRIVATE_ACTIVE, PRIVATE_NOT_ACTIVE)
  }
}
