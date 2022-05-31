package com.tumugin.aisu.infra.repository.exposed

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.CharColumnType
import org.jetbrains.exposed.sql.ExpressionWithColumnType
import org.jetbrains.exposed.sql.QueryBuilder
import org.jetbrains.exposed.sql.append

class DateFormatWithTZFunction<T : ExpressionWithColumnType<Instant>>(
  private val exp: T, private val format: String, private val fromTZ: TimeZone, private val toTZ: TimeZone
) : org.jetbrains.exposed.sql.Function<String>(CharColumnType()) {
  override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
    append("DATE_FORMAT(CONVERT_TZ(", exp, ", '${fromTZ.id}', '${toTZ.id}'), '${format}')")
  }
}
