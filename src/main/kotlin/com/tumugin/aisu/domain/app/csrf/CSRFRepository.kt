package com.tumugin.aisu.domain.app.csrf

interface CSRFRepository {
  fun generateToken(): CSRFToken
  fun validateTokenExists(token: CSRFToken): Boolean
}
