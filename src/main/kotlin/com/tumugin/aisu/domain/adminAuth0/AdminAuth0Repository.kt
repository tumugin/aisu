package com.tumugin.aisu.domain.adminAuth0

interface AdminAuth0Repository {
  suspend fun getAuth0UserInfoByToken(token: AdminAuth0Token): AdminAuth0UserInfo
}
