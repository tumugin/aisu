package com.tumugin.aisu.domain.app.session

interface SessionKVSRepository {
  suspend fun writeSession(id: SessionId, content: SessionContent)
  suspend fun readSession(id: SessionId): SessionContent?
  suspend fun deleteSession(id: SessionId)
}
