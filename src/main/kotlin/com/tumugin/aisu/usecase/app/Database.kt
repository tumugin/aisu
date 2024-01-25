package com.tumugin.aisu.usecase.app

import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import io.lettuce.core.support.BoundedAsyncPool
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class Database : KoinComponent {
  private val jdbcConnectionRepository by inject<JDBCConnectionRepository>()
  private val redisPool by inject<RedisPoolRepository<RedisCoroutinesCommands<String, String>, BoundedAsyncPool<StatefulRedisConnection<String, String>>>>()

  suspend fun prepareDatabase() {
    this.jdbcConnectionRepository.prepareORM()
    this.redisPool.getPoolOrCreate()
  }
}
