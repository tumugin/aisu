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
  private val cachedConnection: StatefulRedisConnection<String, String>? = null

  private suspend fun createConnection(): StatefulRedisConnection<String, String> {
    // Sentinelの認証を上手く扱えないためworkaroundをあてる
    val connectionUri = if (baseConnectionUri.sentinels.isNotEmpty() && baseConnectionUri.password.isNotEmpty()) {
      var sentinelConnectionUriBuilder = RedisURI.builder()
      baseConnectionUri.sentinels.forEach {
        sentinelConnectionUriBuilder =
          sentinelConnectionUriBuilder.withSentinel(it.host, it.port, String(baseConnectionUri.password))
      }
      sentinelConnectionUriBuilder =
        sentinelConnectionUriBuilder
          .withSentinelMasterId(baseConnectionUri.sentinelMasterId)
          .withPassword(baseConnectionUri.password)
      sentinelConnectionUriBuilder.build()
    } else {
      baseConnectionUri
    }

    return RedisClient.create(connectionUri)
      .connectAsync(StringCodec.UTF8, connectionUri)
      .await()
  }

  override suspend fun getConnection(): StatefulRedisConnection<String, String> {
    if (cachedConnection?.isOpen == true) {
      return cachedConnection
    }

    return createConnection()
  }
}
