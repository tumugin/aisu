package com.tumugin.aisu.usecase.app

import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Database : KoinComponent {
  private val jdbcConnectionRepository by inject<JDBCConnectionRepository>()

  fun prepareDatabase() {
    this.jdbcConnectionRepository.prepareORM()
  }
}
