package com.tumugin.aisu.infra.app.session

import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import com.tumugin.aisu.domain.app.session.SessionContent
import com.tumugin.aisu.domain.app.session.SessionId
import com.tumugin.aisu.domain.app.session.SessionKVSRepository
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import io.lettuce.core.support.BoundedAsyncPool
import kotlin.time.Duration.Companion.days

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class SessionKVSRepositoryImpl(
  private val redisPoolRepository: RedisPoolRepository<RedisCoroutinesCommands<String, String>, BoundedAsyncPool<StatefulRedisConnection<String, String>>>
) :
  SessionKVSRepository {
  private val keySuffix = "ss:"
  private val sessionExpireDuration = 30.days

  override suspend fun writeSession(id: SessionId, content: SessionContent) {
    redisPoolRepository.borrow { c ->
      c.set(getKey(id), content.value)
      c.expire(getKey(id), sessionExpireDuration.inWholeSeconds)
    }
  }

  override suspend fun readSession(id: SessionId): SessionContent? {
    return redisPoolRepository.borrow { c ->
      c.get(getKey(id))
    }?.let { SessionContent(it) }
  }

  override suspend fun deleteSession(id: SessionId) {
    redisPoolRepository.borrow { c ->
      c.del(getKey(id))
    }
  }

  private fun getKey(id: SessionId): String {
    return keySuffix + id.value
  }
}
