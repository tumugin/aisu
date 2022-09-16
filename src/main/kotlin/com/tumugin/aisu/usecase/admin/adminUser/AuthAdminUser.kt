package com.tumugin.aisu.usecase.admin.adminUser

import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserEmail
import com.tumugin.aisu.domain.adminUser.AdminUserRawPassword
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AuthAdminUser : KoinComponent {
  private val adminUserRepository: AdminUserRepository by inject()

  suspend fun authAndGetAdminUser(
    adminUserEmail: AdminUserEmail, adminUserRawPassword: AdminUserRawPassword
  ): AdminUser? {
    val adminUser = adminUserRepository.getAdminUserByEmail(adminUserEmail)
    return if (adminUser?.adminUserPassword?.isValid(adminUserRawPassword) == true) {
      adminUser
    } else {
      null
    }
  }
}
