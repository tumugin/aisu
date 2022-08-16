package com.tumugin.aisu.app.serializer

import kotlinx.serialization.Serializable

@Serializable
abstract class PaginationSerializer(
  val currentPage: Int, val pageCount: Int
) {}
