package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.app.serializer.PaginationSerializer

class GroupPaginationSerializer(currentPage: Int, pageCount: Int, val groups: List<GroupSerializer>) :
  PaginationSerializer(currentPage, pageCount) {}
