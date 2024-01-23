package com.tumugin.aisu.infra.app.csrf

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import io.lettuce.core.api.StatefulRedisConnection
import kotlinx.coroutines.future.await
import java.util.*
import kotlin.time.Duration.Companion.hours

class CSRFRepositoryImpl(private val redisPoolRepository: RedisPoolRepository<StatefulRedisConnection<String, String>>) :
  CSRFRepository {
  private val keySuffix = "csrf:"
  private val sessionExpireDuration = 12.hours

  override suspend fun generateToken(): CSRFToken {
    val connection = redisPoolRepository.getConnection().async()
    val token = CSRFToken(UUID.randomUUID().toString())
    connection.set(getKey(token), "").await()
    connection.expire(getKey(token), sessionExpireDuration.inWholeSeconds).await()

    return token
  }

  override suspend fun validateTokenExists(token: CSRFToken): Boolean {
    val connection = redisPoolRepository.getConnection().async()
    return connection.exists(getKey(token)).await() > 0
  }

  private fun getKey(id: CSRFToken): String {
    return keySuffix + id.value
  }
}
