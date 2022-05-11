package com.tumugin.aisu.domain.app.database

interface RedisPoolRepository<T> {
  val redisPool: T
}
