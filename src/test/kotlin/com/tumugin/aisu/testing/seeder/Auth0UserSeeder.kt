package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.auth0.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class Auth0UserSeeder : KoinComponent {
  private val auth0UserRepository by inject<Auth0UserRepository>()

  suspend fun seedAuth0User(
    auth0UserId: Auth0UserId = Auth0UserId("${UUID.randomUUID()}|test"),
    auth0UserName: Auth0UserName = Auth0UserName("Aoi Suzu")
  ): Auth0User {
    return auth0UserRepository.addAuth0User(
      Auth0UserInfo(
        auth0UserId = auth0UserId, auth0UserName = auth0UserName
      )
    )
  }
}
