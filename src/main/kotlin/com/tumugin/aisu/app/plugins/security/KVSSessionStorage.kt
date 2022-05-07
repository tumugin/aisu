package com.tumugin.aisu.app.plugins.security

import com.tumugin.aisu.domain.app.session.SessionContent
import com.tumugin.aisu.domain.app.session.SessionId
import com.tumugin.aisu.domain.app.session.SessionKVSRepository
import io.ktor.server.sessions.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class KVSSessionStorage : SessionStorage, KoinComponent {
  private val sessionKVSRepository by inject<SessionKVSRepository>()

  override suspend fun invalidate(id: String) {
    sessionKVSRepository.deleteSession(SessionId(id))
  }

  override suspend fun read(id: String): String {
    return sessionKVSRepository.readSession(SessionId(id))?.value
      ?: throw NoSuchElementException("Session $id not found")
  }

  override suspend fun write(id: String, value: String) {
    sessionKVSRepository.writeSession(SessionId(id), SessionContent(value))
  }
}
