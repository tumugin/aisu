package com.tumugin.aisu.infra.repository.exposed

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.offsetAt
import org.jetbrains.exposed.v1.core.CharColumnType
import org.jetbrains.exposed.v1.core.ExpressionWithColumnType
import org.jetbrains.exposed.v1.core.QueryBuilder
import org.jetbrains.exposed.v1.core.append
import org.jetbrains.exposed.v1.core.vendors.MysqlDialect
import org.jetbrains.exposed.v1.core.vendors.PostgreSQLDialect
import org.jetbrains.exposed.v1.core.vendors.currentDialect
import java.time.OffsetDateTime

class DateFormatWithTZFunction<T : ExpressionWithColumnType<OffsetDateTime>>(
  private val exp: T, private val format: String, private val fromTZ: TimeZone, private val toTZ: TimeZone
) : org.jetbrains.exposed.v1.core.Function<String>(CharColumnType()) {
  override fun toQueryBuilder(queryBuilder: QueryBuilder) = queryBuilder {
    when (currentDialect.name) {
      PostgreSQLDialect.dialectName -> {
        append(
          "TO_CHAR(",
          exp,
          " AT TIME ZONE INTERVAL '${toOffsetString(toTZ)}'",
          ", '${format}')"
        )
      }

      MysqlDialect.dialectName -> {
        append(
          "DATE_FORMAT(CONVERT_TZ(",
          exp,
          ", '${toOffsetString(fromTZ)}', '${toOffsetString(toTZ)}'), '${format}')"
        )
      }

      else -> {
        error("${currentDialect.name} is unsupported database for DateFormatWithTZFunction.")
      }
    }
  }

  private fun toOffsetString(tz: TimeZone): String {
    val converted = tz.offsetAt(Clock.System.now()).toString()
    if (converted == "Z") {
      return "+00:00"
    }
    return converted
  }
}
