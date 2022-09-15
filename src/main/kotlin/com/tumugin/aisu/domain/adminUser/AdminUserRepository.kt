package com.tumugin.aisu.domain.adminUser

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult

interface AdminUserRepository {
  suspend fun getAdminUserById(adminUserId: AdminUserId): AdminUser?

  suspend fun getAdminUsersByIds(adminUserIds: List<AdminUserId>): List<AdminUser>

  suspend fun getAdminUserByEmail(adminUserEmail: AdminUserEmail): AdminUser?

  suspend fun addAdminUser(
    adminUserName: AdminUserName, adminUserEmail: AdminUserEmail, adminUserPassword: AdminUserPassword
  ): AdminUser

  suspend fun updateAdminUser(
    adminUserId: AdminUserId,
    adminUserName: AdminUserName,
    adminUserEmail: AdminUserEmail,
    adminUserPassword: AdminUserPassword
  ): AdminUser

  suspend fun deleteAdminUser(adminUserId: AdminUserId)

  suspend fun getAllAdminUsers(paginatorParam: PaginatorParam): PaginatorResult<AdminUser>
}
