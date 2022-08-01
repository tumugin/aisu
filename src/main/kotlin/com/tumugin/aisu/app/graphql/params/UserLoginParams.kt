package com.tumugin.aisu.app.graphql.params

import com.tumugin.aisu.app.request.api.LoginRequest

class UserLoginParams(val email: String, val password: String) {
  fun toLoginRequest() = LoginRequest(email, password)
}
