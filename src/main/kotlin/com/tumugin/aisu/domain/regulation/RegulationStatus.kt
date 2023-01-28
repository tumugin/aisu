package com.tumugin.aisu.domain.regulation

enum class RegulationStatus(s: String) {
  /**
   * 有効ではないレギュ
   */
  NOT_ACTIVE("not_active"),

  /**
   * 有効なレギュ
   */
  ACTIVE("active"),

  /**
   * 運営削除(論理削除)
   */
  OPERATION_DELETED("operation_deleted");

  companion object {
    val allUserCanSpecifyStatus = listOf(NOT_ACTIVE, ACTIVE)
    val allStatuses = listOf(NOT_ACTIVE, ACTIVE, OPERATION_DELETED)
  }
}
