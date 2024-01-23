package com.tumugin.aisu.infra.app.session

import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import com.tumugin.aisu.domain.app.session.SessionContent
import com.tumugin.aisu.domain.app.session.SessionId
import com.tumugin.aisu.domain.app.session.SessionKVSRepository
import redis.clients.jedis.Jedis
import redis.clients.jedis.util.Pool
import kotlin.time.Duration.Companion.days

class SessionKVSRepositoryImpl(redisPoolRepository: RedisPoolRepository<Pool<Jedis>>) : SessionKVSRepository {
  private val jedisPool = redisPoolRepository.redisPool
  private val keySuffix = "ss:"
  private val sessionExpireDuration = 30.days

  override suspend fun writeSession(id: SessionId, content: SessionContent) {
    jedisPool.resource.use { jedis ->
      jedis[getKey(id)] = content.value
      jedis.expire(getKey(id), sessionExpireDuration.inWholeSeconds)
    }
  }

  override suspend fun readSession(id: SessionId): SessionContent? {
    return jedisPool.resource.use { jedis ->
      jedis.expire(getKey(id), sessionExpireDuration.inWholeSeconds)
      return@use jedis[getKey(id)]?.let { SessionContent(it) }
    }
  }

  override suspend fun deleteSession(id: SessionId) {
    jedisPool.resource.use { jedis ->
      jedis.del(getKey(id))
    }
  }

  private fun getKey(id: SessionId): String {
    return keySuffix + id.value
  }
}
