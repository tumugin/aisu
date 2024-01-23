package com.tumugin.aisu.domain.app.database

interface RedisPoolRepository<T> {
  suspend fun getConnection(): T
}
