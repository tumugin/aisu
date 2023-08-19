package com.tumugin.aisu.domain.auth0

interface Auth0Repository {
  fun generateAuth0LogoutUrl(): Auth0LogoutUrl
  suspend fun requestPasswordReset(auth0MailAddress: Auth0MailAddress)
}
