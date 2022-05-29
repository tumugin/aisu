package com.tumugin.aisu.domain.regulation

enum class RegulationStatus(s: String) {
  /**
   * 公開状態かつ有効(現在有効なレギュ)
   */
  PUBLIC_ACTIVE("public_active"),

  /**
   * 公開状態かつ有効でない(今は使われてないレギュ)
   */
  PUBLIC_NOT_ACTIVE("public_not_active"),

  /**
   * 非公開状態(作ったユーザのみが閲覧可能)かつ有効(現在有効なレギュ)
   */
  PRIVATE_ACTIVE("private_active"),

  /**
   * 非公開状態(作ったユーザのみが閲覧可能)かつ有効でない(今は使われてないレギュ)
   */
  PRIVATE_NOT_ACTIVE("private_not_active"),

  /**
   * 運営削除(論理削除)
   */
  OPERATION_DELETED("operation_deleted")
}
