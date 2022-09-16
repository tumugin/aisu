package com.tumugin.aisu.usecase.admin.adminUser

import com.tumugin.aisu.domain.adminUser.*
import org.koin.core.component.KoinComponent

class CreateAdminUser : KoinComponent {
  private val adminUserDomainService = AdminUserDomainService()

  suspend fun createAdminUser(
    adminUserName: AdminUserName,
    adminUserEmail: AdminUserEmail,
    adminUserPassword: AdminUserPassword,
  ): AdminUser {
    return adminUserDomainService.createAdminUser(
      adminUserName,
      adminUserEmail,
      adminUserPassword,
    )
  }
}
