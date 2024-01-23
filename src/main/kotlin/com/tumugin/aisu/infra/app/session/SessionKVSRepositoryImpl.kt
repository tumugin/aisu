package com.tumugin.aisu.infra.app.session

import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import com.tumugin.aisu.domain.app.session.SessionContent
import com.tumugin.aisu.domain.app.session.SessionId
import com.tumugin.aisu.domain.app.session.SessionKVSRepository
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.future.await
import kotlin.time.Duration.Companion.days

class SessionKVSRepositoryImpl(private val redisPoolRepository: RedisPoolRepository<StatefulRedisConnection<String, String>>) :
  SessionKVSRepository {
  private val keySuffix = "ss:"
  private val sessionExpireDuration = 30.days

  override suspend fun writeSession(id: SessionId, content: SessionContent) {
    val connection = redisPoolRepository.getConnection().async()
    connection.set(getKey(id), content.value).await()
    connection.expire(getKey(id), sessionExpireDuration.inWholeSeconds).await()
  }

  override suspend fun readSession(id: SessionId): SessionContent? {
    val connection = redisPoolRepository.getConnection().async()
    return connection.get(getKey(id)).await()?.let { SessionContent(it) }
  }

  override suspend fun deleteSession(id: SessionId) {
    val connection = redisPoolRepository.getConnection().async()
    connection.del(getKey(id)).await()
  }

  private fun getKey(id: SessionId): String {
    return keySuffix + id.value
  }
}
