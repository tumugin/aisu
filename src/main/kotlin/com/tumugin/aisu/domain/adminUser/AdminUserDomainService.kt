package com.tumugin.aisu.domain.adminUser

import com.tumugin.aisu.domain.user.UserAlreadyExistException
import com.tumugin.aisu.domain.user.UserNotFoundException
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

  suspend fun updateAdminUser(
    adminUserId: AdminUserId, adminUserName: AdminUserName, adminUserEmail: AdminUserEmail
  ): AdminUser {
    val existingAdminUser = adminUserRepository.getAdminUserById(adminUserId) ?: throw UserNotFoundException()
    return adminUserRepository.updateAdminUser(
      existingAdminUser.adminUserId, adminUserName, adminUserEmail, existingAdminUser.adminUserPassword
    )
  }

  suspend fun updateAdminUserPassword(adminUserId: AdminUserId, adminUserRawPassword: AdminUserRawPassword): AdminUser {
    val existingAdminUser = adminUserRepository.getAdminUserById(adminUserId) ?: throw UserNotFoundException()
    return adminUserRepository.updateAdminUser(
      existingAdminUser.adminUserId,
      existingAdminUser.adminUserName,
      existingAdminUser.adminUserEmail,
      adminUserRawPassword.toHashedPassword()
    )
  }
}
