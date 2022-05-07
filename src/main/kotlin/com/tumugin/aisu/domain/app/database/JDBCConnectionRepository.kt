package com.tumugin.aisu.domain.app.database

import javax.sql.DataSource

interface JDBCConnectionRepository {
  val dataSource: DataSource
  fun prepareORM()
}
