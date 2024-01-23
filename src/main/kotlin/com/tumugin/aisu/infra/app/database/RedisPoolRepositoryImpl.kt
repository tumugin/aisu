package com.tumugin.aisu.infra.app.database

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisSentinelPool
import redis.clients.jedis.util.Pool

class RedisPoolRepositoryImpl(private val appConfigRepository: AppConfigRepository) : RedisPoolRepository<Pool<Jedis>> {
  override val redisPool by lazy {
    val pool = if (appConfigRepository.appConfig.appConfigEnableRedisSentinel.value) {
      JedisSentinelPool(
        appConfigRepository.appConfig.appConfigRedisSentinelMasterName.value,
        setOf(appConfigRepository.appConfig.appConfigRedisConnectionUrl.value)
      )
    } else {
      JedisPool(
        appConfigRepository.appConfig.appConfigRedisConnectionUrl.value
      )
    }
    pool.testOnBorrow = true
    pool
  }
}
