package com.tumugin.aisu.domain.app.csrf

interface CSRFRepository {
  suspend fun generateToken(): CSRFToken
  suspend fun validateTokenExists(token: CSRFToken): Boolean
}
