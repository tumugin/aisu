package com.tumugin.aisu.domain.user

data class User(
  val userId: UserId,
  val userName: UserName,
  val userEmail: UserEmail,
  val userPassword: UserPassword,
  val userEmailVerifiedAt: UserEmailVerifiedAt,
  val userRememberToken: UserRememberToken,
  val userCreatedAt: UserCreatedAt,
  val userUpdatedAt: UserUpdatedAt
)
