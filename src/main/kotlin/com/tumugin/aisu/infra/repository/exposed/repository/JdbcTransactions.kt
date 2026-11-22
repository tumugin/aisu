package com.tumugin.aisu.infra.repository.exposed.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.inTopLevelSuspendTransaction

internal suspend fun <T> withJdbcTransaction(statement: suspend JdbcTransaction.() -> T): T {
  return withContext(Dispatchers.IO) {
    // Repository calls commit independently of any enclosing transaction.
    inTopLevelSuspendTransaction(statement = statement)
  }
}
