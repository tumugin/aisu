package com.tumugin.aisu.usecase.admin.adminUser

import com.tumugin.aisu.domain.adminAuth0.AdminAuth0Repository
import com.tumugin.aisu.domain.adminAuth0.AdminAuth0Token
import com.tumugin.aisu.domain.adminUser.*
import io.ktor.server.auth.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginOrCreateAdminUserByAuth0Callback : KoinComponent {
  private val adminAuth0Repository by inject<AdminAuth0Repository>()
  private val adminUserRepository by inject<AdminUserRepository>()
  private val adminUserDomainService by lazy { AdminUserDomainService() }

  suspend fun getOrCreateAdminUserByPrincipal(principal: OAuthAccessTokenResponse.OAuth2): AdminUser {
    val auth0UserInfo = adminAuth0Repository.getAuth0UserInfoByToken(AdminAuth0Token(principal.accessToken))
    val foundAdminUser = adminUserRepository.getAdminUserByEmail(AdminUserEmail(auth0UserInfo.adminAuth0Email.value))
    if (foundAdminUser != null) {
      return foundAdminUser
    }
    return adminUserDomainService.createAdminUser(
      AdminUserName(auth0UserInfo.adminAuth0UserName.value),
      AdminUserEmail(auth0UserInfo.adminAuth0Email.value),
      null
    )
  }
}
