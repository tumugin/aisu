package com.tumugin.aisu.domain.app.database

interface RedisPoolRepository<T, X> {
  suspend fun getPoolOrCreate(): X
  suspend fun <Z> borrow(proc: suspend (connection: T) -> Z): Z
}
