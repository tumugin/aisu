package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.app.serializer.PaginationSerializer

class IdolPaginationSerializer(currentPage: Int, pageCount: Int, count: Int, val idols: List<IdolSerializer>) :
  PaginationSerializer(currentPage, pageCount, count) {}
