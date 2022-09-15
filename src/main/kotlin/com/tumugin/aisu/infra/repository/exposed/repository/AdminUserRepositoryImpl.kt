package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.adminUser.*
import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.infra.repository.exposed.models.AdminUsers
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.AdminUser as AdminUserModel

class AdminUserRepositoryImpl : AdminUserRepository {
  override suspend fun getAdminUserById(adminUserId: AdminUserId): AdminUser? {
    return transaction {
      AdminUserModel.findById(adminUserId.value)?.toDomain()
    }
  }

  override suspend fun getAdminUsersByIds(adminUserIds: List<AdminUserId>): List<AdminUser> {
    return transaction {
      AdminUserModel.find { AdminUsers.id inList adminUserIds.map { it.value } }.map { it.toDomain() }
    }
  }

  override suspend fun getAdminUserByEmail(adminUserEmail: AdminUserEmail): AdminUser? {
    return transaction {
      AdminUserModel.find { AdminUsers.email eq adminUserEmail.value }.firstOrNull()?.toDomain()
    }
  }

  override suspend fun addAdminUser(
    adminUserName: AdminUserName, adminUserEmail: AdminUserEmail, adminUserPassword: AdminUserPassword
  ): AdminUser {
    return transaction {
      AdminUserModel.new {
        name = adminUserName.value
        email = adminUserEmail.value
        password = adminUserPassword.value
      }.toDomain()
    }
  }

  override suspend fun updateAdminUser(
    adminUserId: AdminUserId,
    adminUserName: AdminUserName,
    adminUserEmail: AdminUserEmail,
    adminUserPassword: AdminUserPassword
  ): AdminUser {
    return transaction {
      AdminUserModel[adminUserId.value].apply {
        name = adminUserName.value
        email = adminUserEmail.value
        password = adminUserPassword.value
      }.toDomain()
    }
  }

  override suspend fun deleteAdminUser(adminUserId: AdminUserId) {
    transaction {
      AdminUserModel[adminUserId.value].delete()
    }
  }

  override suspend fun getAllAdminUsers(paginatorParam: PaginatorParam): PaginatorResult<AdminUser> {
    return transaction {
      val query = AdminUserModel.all()
      val count = query.count()
      val results = query.limit(paginatorParam.limit.toInt(), paginatorParam.offset).map { it.toDomain() }
      paginatorParam.createPaginatorResult(
        count, results
      )
    }
  }
}
