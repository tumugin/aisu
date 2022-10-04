package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.auth0.Auth0User
import com.tumugin.aisu.domain.auth0.Auth0UserInfo
import com.tumugin.aisu.domain.auth0.Auth0UserRepository
import com.tumugin.aisu.infra.repository.exposed.models.Auth0Users
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Auth0User as Auth0UserModel
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel

class Auth0UserRepositoryImpl : Auth0UserRepository {
  override suspend fun getAuth0User(auth0UserInfo: Auth0UserInfo): Auth0User? {
    return transaction {
      Auth0UserModel
        .find { Auth0Users.auth0UserId eq auth0UserInfo.auth0UserId.value }
        .with(Auth0UserModel::user)
        .firstOrNull()
        ?.toDomain()
    }
  }

  override suspend fun addAuth0User(auth0UserInfo: Auth0UserInfo): Auth0User {
    return transaction {
      val user = UserModel.new {
        this.name = auth0UserInfo.auth0UserName.value
      }
      Auth0UserModel.new {
        this.user = user
        this.auth0UserId = auth0UserInfo.auth0UserId.value
      }.toDomain()
    }
  }
}
