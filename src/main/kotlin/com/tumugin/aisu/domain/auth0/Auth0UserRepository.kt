package com.tumugin.aisu.domain.auth0

import com.tumugin.aisu.domain.user.UserId

interface Auth0UserRepository {
  suspend fun getAuth0User(auth0UserInfo: Auth0UserInfo): Auth0User?
  suspend fun getAuth0UserByUserId(userId: UserId): Auth0User?
  suspend fun addAuth0User(auth0UserInfo: Auth0UserInfo): Auth0User
}
