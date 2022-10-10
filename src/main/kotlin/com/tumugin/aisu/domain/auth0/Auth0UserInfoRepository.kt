package com.tumugin.aisu.domain.auth0

interface Auth0UserInfoRepository {
  suspend fun getAuth0UserInfoByToken(token: Auth0Token): Auth0UserInfo
}
