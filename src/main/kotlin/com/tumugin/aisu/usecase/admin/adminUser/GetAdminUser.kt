package com.tumugin.aisu.usecase.admin.adminUser

import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetAdminUser : KoinComponent {
  private val adminUserRepository: AdminUserRepository by inject()

  suspend fun getAdminUserByAdminUserId(adminUserId: AdminUserId): AdminUser? {
    return adminUserRepository.getAdminUserById(adminUserId)
  }

  suspend fun getAdminUsersByAdminUserIds(adminUserIds: List<AdminUserId>): List<AdminUser> {
    return adminUserRepository.getAdminUsersByIds(adminUserIds)
  }

  suspend fun getAllAdminUsers(paginatorParam: PaginatorParam): PaginatorResult<AdminUser> {
    return adminUserRepository.getAllAdminUsers(paginatorParam)
  }
}
