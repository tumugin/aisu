package com.tumugin.aisu.domain.base

data class PaginatorResult<T>(val count: Long, val limit: Long, val page: Long, val result: List<T>) {
  val pages: Long = count / limit + if (count % limit != 0L) 1 else 0
}
