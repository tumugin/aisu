package com.tumugin.aisu.usecase.admin.adminUser

import com.tumugin.aisu.domain.adminUser.*
import org.koin.core.component.KoinComponent

class UpdateAdminUser : KoinComponent {
  private val adminUserDomainService = AdminUserDomainService()

  suspend fun updateAdminUser(
    adminUserId: AdminUserId, adminUserName: AdminUserName, adminUserEmail: AdminUserEmail
  ): AdminUser {
    return adminUserDomainService.updateAdminUser(adminUserId, adminUserName, adminUserEmail)
  }

  suspend fun updateAdminUserPassword(adminUserId: AdminUserId, adminUserRawPassword: AdminUserRawPassword): AdminUser {
    return adminUserDomainService.updateAdminUserPassword(adminUserId, adminUserRawPassword)
  }
}
