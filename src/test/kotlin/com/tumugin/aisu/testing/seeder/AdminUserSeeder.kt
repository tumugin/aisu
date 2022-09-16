package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.adminUser.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class AdminUserSeeder : KoinComponent {
  private val adminUserRepository: AdminUserRepository by inject()

  suspend fun seedAdminUser(
    adminUserName: AdminUserName = AdminUserName("藍井すず"),
    adminUserEmail: AdminUserEmail = AdminUserEmail("aoisuzu@example.com"),
    adminUserRawPassword: AdminUserRawPassword = AdminUserRawPassword("password")
  ): AdminUser {
    return adminUserRepository.addAdminUser(
      adminUserName = adminUserName,
      adminUserEmail = adminUserEmail,
      adminUserPassword = adminUserRawPassword.toHashedPassword()
    )
  }

  suspend fun seedNonDuplicateAdminUser(
    adminUserName: AdminUserName = AdminUserName("藍井すず"),
    adminUserRawPassword: AdminUserRawPassword = AdminUserRawPassword("password")
  ): AdminUser {
    return adminUserRepository.addAdminUser(
      adminUserName = adminUserName,
      adminUserEmail = AdminUserEmail("${UUID.randomUUID()}@example.com"),
      adminUserPassword = adminUserRawPassword.toHashedPassword()
    )
  }
}
