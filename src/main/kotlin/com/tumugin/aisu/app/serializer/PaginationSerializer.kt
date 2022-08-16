package com.tumugin.aisu.app.serializer

import kotlinx.serialization.Serializable

@Serializable
data class PaginationSerializer<T>(
  val currentPage: Int, val pageCount: Int, val result: T
) {}
