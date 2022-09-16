package com.tumugin.aisu.domain.adminUser

import com.tumugin.aisu.domain.user.UserAlreadyExistException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AdminUserDomainService : KoinComponent {
  private val adminUserRepository: AdminUserRepository by inject()

  suspend fun createAdminUser(
    adminUserName: AdminUserName,
    adminUserEmail: AdminUserEmail,
    adminUserPassword: AdminUserPassword,
  ): AdminUser {
    val existingAdminUser = adminUserRepository.getAdminUserByEmail(adminUserEmail)
    if (existingAdminUser != null) {
      throw UserAlreadyExistException()
    }
    return adminUserRepository.addAdminUser(
      adminUserName,
      adminUserEmail,
      adminUserPassword,
    )
  }
}
