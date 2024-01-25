package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.auth0.Auth0User
import com.tumugin.aisu.domain.auth0.Auth0UserInfo
import com.tumugin.aisu.domain.auth0.Auth0UserRepository
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.Auth0Users
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.koin.core.component.KoinComponent
import com.tumugin.aisu.infra.repository.exposed.models.Auth0User as Auth0UserModel
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel

class Auth0UserRepositoryImpl : Auth0UserRepository, KoinComponent {
  override suspend fun getAuth0User(auth0UserInfo: Auth0UserInfo): Auth0User? {
    return newSuspendedTransaction(Dispatchers.IO) {
      Auth0UserModel
        .find { Auth0Users.auth0UserId eq auth0UserInfo.auth0UserId.value }
        .with(Auth0UserModel::user)
        .firstOrNull()
        ?.toDomain()
    }
  }

  override suspend fun getAuth0UserByUserId(userId: UserId): Auth0User? {
    return newSuspendedTransaction(Dispatchers.IO) {
      Auth0UserModel
        .find { Auth0Users.user eq userId.value }
        .with(Auth0UserModel::user)
        .firstOrNull()
        ?.toDomain()
    }
  }

  override suspend fun addAuth0User(auth0UserInfo: Auth0UserInfo): Auth0User {
    return newSuspendedTransaction(Dispatchers.IO) {
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
