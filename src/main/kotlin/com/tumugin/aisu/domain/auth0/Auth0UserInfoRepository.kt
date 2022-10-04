package com.tumugin.aisu.domain.auth0

interface Auth0UserInfoRepository {
  suspend fun getAuth0UserInfoByToken(token: String): Auth0UserInfo
}
