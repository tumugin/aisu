package com.tumugin.aisu.infra.app.database

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.RedisReadOnlyException
import io.lettuce.core.RedisURI
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import io.lettuce.core.codec.StringCodec
import io.lettuce.core.support.AsyncConnectionPoolSupport
import io.lettuce.core.support.BoundedAsyncPool
import io.lettuce.core.support.BoundedPoolConfig
import kotlinx.coroutines.future.await
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class RedisPoolRepositoryImpl(appConfigRepository: AppConfigRepository) :
  RedisPoolRepository<RedisCoroutinesCommands<String, String>, BoundedAsyncPool<StatefulRedisConnection<String, String>>> {
  private val baseConnectionUri = RedisURI.create(appConfigRepository.appConfig.appConfigRedisConnectionUrl.value)
  private var pool: BoundedAsyncPool<StatefulRedisConnection<String, String>>? = null
  private val maxConnections = 10
  private val semaphore = Semaphore(maxConnections)

  private suspend fun createPool(): BoundedAsyncPool<StatefulRedisConnection<String, String>> {
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

    val poolConfig = BoundedPoolConfig.builder()
      .maxTotal(maxConnections)
      .testOnAcquire()
      .build()

    val createdPool = AsyncConnectionPoolSupport.createBoundedObjectPoolAsync({
      RedisClient.create(connectionUri)
        .connectAsync(StringCodec.UTF8, connectionUri)
    }, poolConfig).await()

    pool = createdPool

    return createdPool
  }

  override suspend fun getPoolOrCreate(): BoundedAsyncPool<StatefulRedisConnection<String, String>> {
    val currentPool = pool ?: return createPool()

    return currentPool
  }

  @OptIn(ExperimentalLettuceCoroutinesApi::class)
  override suspend fun <Z> borrow(proc: suspend (connection: RedisCoroutinesCommands<String, String>) -> Z): Z {
    semaphore.withPermit {
      val pool = getPoolOrCreate()
      val connection = pool.acquire().await()
      try {
        return proc(connection.coroutines())
      } catch (e: RedisReadOnlyException) {
        createPool()
        throw e
      } finally {
        pool.release(connection).await()
      }
    }
  }
}
