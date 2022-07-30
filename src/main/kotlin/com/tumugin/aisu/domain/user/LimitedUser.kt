package com.tumugin.aisu.domain.user

data class LimitedUser(
  val userId: UserId,
  val userName: UserName,
) {
  companion object {
    fun createFromUser(user: User): LimitedUser {
      return LimitedUser(
        userId = user.userId,
        userName = user.userName
      )
    }
  }
}
