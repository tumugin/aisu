package com.tumugin.aisu.app.serializer.admin

import com.tumugin.aisu.app.serializer.PaginationSerializer

class AdminUserPaginationSerializer(
  currentPage: Int,
  pageCount: Int,
  count: Int,
  val adminUsers: List<AdminUserSerializer>
) :
  PaginationSerializer(currentPage, pageCount, count) {}
