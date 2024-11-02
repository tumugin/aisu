package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.app.serializer.PaginationSerializer

class ChekiPaginationSerializer(currentPage: Int, pageCount: Int, count: Int, val chekis: List<ChekiSerializer>) :
  PaginationSerializer(currentPage, pageCount, count)
