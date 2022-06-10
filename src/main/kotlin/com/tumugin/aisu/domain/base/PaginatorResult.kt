package com.tumugin.aisu.domain.base

data class PaginatorResult<T>(val count: Long, val limit: Long, val page: Long, val result: List<T>) {}
