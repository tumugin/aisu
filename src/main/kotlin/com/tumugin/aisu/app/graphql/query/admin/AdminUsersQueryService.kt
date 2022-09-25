package com.tumugin.aisu.app.graphql.query.admin

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.serializer.admin.AdminUserPaginationSerializer
import com.tumugin.aisu.app.serializer.admin.AdminUserSerializer
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.usecase.admin.adminUser.GetAdminUser
import graphql.schema.DataFetchingEnvironment

class AdminUsersQueryService : Query {
  private val getAdminUser = GetAdminUser()

  suspend fun getAdminUser(adminUserID: ID): AdminUserSerializer? {
    val adminUser = getAdminUser.getAdminUserByAdminUserId(
      AdminUserId(adminUserID.value.toLong())
    )
    return adminUser?.let { AdminUserSerializer.from(it) }
  }

  suspend fun getAdminUserList(dfe: DataFetchingEnvironment, page: Int): AdminUserPaginationSerializer {
    val adminUsers = getAdminUser.getAllAdminUsers(PaginatorParam(page.toLong(), 10))
    return AdminUserPaginationSerializer(page,
      adminUsers.pages.toInt(),
      adminUsers.result.map { AdminUserSerializer.from(it) })
  }
}
