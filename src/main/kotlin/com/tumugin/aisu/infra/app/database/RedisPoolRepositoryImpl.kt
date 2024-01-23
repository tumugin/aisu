package com.tumugin.aisu.infra.app.database

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.codec.StringCodec
import kotlinx.coroutines.future.await

class RedisPoolRepositoryImpl(appConfigRepository: AppConfigRepository) :
  RedisPoolRepository<StatefulRedisConnection<String, String>> {
  private val baseConnectionUri = RedisURI.create(appConfigRepository.appConfig.appConfigRedisConnectionUrl.value)
  private val sentinelEnabled = appConfigRepository.appConfig.appConfigEnableRedisSentinel.value
  private val masterName = appConfigRepository.appConfig.appConfigRedisSentinelMasterName.value

  private val cachedConnection: StatefulRedisConnection<String, String>? = null

  private suspend fun createConnection(): StatefulRedisConnection<String, String> {
    val uri = if (sentinelEnabled) {
      RedisURI.builder()
        .withSentinel(baseConnectionUri)
        .withSentinelMasterId(masterName)
        .build()
    } else {
      baseConnectionUri
    }

    return RedisClient.create(uri)
      .connectAsync(StringCodec.UTF8, uri)
      .await()
  }

  override suspend fun getConnection(): StatefulRedisConnection<String, String> {
    if (cachedConnection?.isOpen == true) {
      return cachedConnection
    }

    return createConnection()
  }
}
