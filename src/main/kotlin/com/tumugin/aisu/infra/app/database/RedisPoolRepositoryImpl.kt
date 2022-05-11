package com.tumugin.aisu.infra.app.database

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import redis.clients.jedis.JedisPool

class RedisPoolRepositoryImpl(private val appConfigRepository: AppConfigRepository) : RedisPoolRepository<JedisPool> {
  override val redisPool by lazy {
    JedisPool(
      appConfigRepository.appConfig.appConfigRedisHost.value,
      appConfigRepository.appConfig.appConfigRedisPort.value
    )
  }
}
