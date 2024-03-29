package com.tumugin.aisu.domain.auth0

import com.tumugin.aisu.domain.user.User

data class Auth0User(
  val user: User, val auth0UserId: Auth0UserId
) {}
