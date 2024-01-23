package com.tumugin.aisu.usecase.app

import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import com.tumugin.aisu.domain.app.database.RedisPoolRepository
import io.lettuce.core.api.StatefulRedisConnection
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Database : KoinComponent {
  private val jdbcConnectionRepository by inject<JDBCConnectionRepository>()
  private val redisPool by inject<RedisPoolRepository<StatefulRedisConnection<String, String>>>()

  suspend fun prepareDatabase() {
    this.jdbcConnectionRepository.prepareORM()
    this.redisPool.getConnection()
  }
}
