package com.tumugin.aisu.infra.app.csrf

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import io.lettuce.core.support.BoundedAsyncPool
import java.util.*
import kotlin.time.Duration.Companion.hours

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class CSRFRepositoryImpl(
  private val redisPoolRepository: RedisPoolRepository<RedisCoroutinesCommands<String, String>, BoundedAsyncPool<StatefulRedisConnection<String, String>>>
) :
  CSRFRepository {
  private val keySuffix = "csrf:"
  private val sessionExpireDuration = 12.hours

  override suspend fun generateToken(): CSRFToken {
    val token = CSRFToken(UUID.randomUUID().toString())
    redisPoolRepository.borrow { c ->
      c.set(getKey(token), "")
      c.expire(getKey(token), sessionExpireDuration.inWholeSeconds)
    }

    return token
  }

  override suspend fun validateTokenExists(token: CSRFToken): Boolean {
    return redisPoolRepository.borrow { c ->
      c.exists(getKey(token)) == 1.toLong()
    }
  }

  private fun getKey(id: CSRFToken): String {
    return keySuffix + id.value
  }
}
