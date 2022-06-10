package com.tumugin.aisu.domain.base

data class PaginatorParam(val page: Long, val limit: Long) {
  init {
    if (page < 1) {
      throw IllegalArgumentException("page should not be less than 1")
    }
    if (limit < 0) {
      throw IllegalArgumentException("limit should not be less than 0")
    }
  }

  val offset: Long
    get() {
      return limit * (page - 1)
    }

  fun <T> createPaginatorResult(count: Long, result: List<T>): PaginatorResult<T> {
    return PaginatorResult(
      count = count,
      limit = limit,
      page = page,
      result = result
    )
  }
}
