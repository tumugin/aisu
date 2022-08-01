package com.tumugin.aisu.app.graphql.params

import com.tumugin.aisu.app.request.api.CreateUserRequest

class UserCreateParams(val email: String, val password: String, val name: String) {
  fun toCreateUserRequest(): CreateUserRequest {
    return CreateUserRequest(
        email = email,
        password = password,
        name = name
    )
  }
}
