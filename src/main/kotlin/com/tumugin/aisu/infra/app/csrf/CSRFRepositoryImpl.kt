package com.tumugin.aisu.infra.app.csrf

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import redis.clients.jedis.Jedis
import redis.clients.jedis.util.Pool
import java.util.*
import kotlin.time.Duration.Companion.hours

class CSRFRepositoryImpl(redisPoolRepository: RedisPoolRepository<Pool<Jedis>>) : CSRFRepository {
  private val jedisPool = redisPoolRepository.redisPool
  private val keySuffix = "csrf:"
  private val sessionExpireDuration = 12.hours

  override fun generateToken(): CSRFToken {
    val token = CSRFToken(UUID.randomUUID().toString())
    jedisPool.resource.use { jedis ->
      jedis[getKey(token)] = ""
      jedis.expire(getKey(token), sessionExpireDuration.inWholeSeconds)
    }
    return token
  }

  override fun validateTokenExists(token: CSRFToken): Boolean {
    return jedisPool.resource.use { jedis ->
      jedis.expire(getKey(token), sessionExpireDuration.inWholeSeconds)
      return@use jedis.exists(getKey(token))
    }
  }

  private fun getKey(id: CSRFToken): String {
    return keySuffix + id.value
  }
}
