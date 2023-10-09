package com.tumugin.aisu.infra.repository.exposed

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.vendors.*
import java.sql.ResultSet
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class DataTimeWithTimeZoneColumnType : ColumnType(), IDateColumnType {
  override val hasTimePart: Boolean = true

  override fun nonNullValueToString(value: Any): String {
    if (value !is Instant) {
      error("$value is not a Instant!")
    }
    return "'${DateTimeFormatter.ISO_INSTANT.format(value.toJavaInstant())}'"
  }

  override fun valueFromDB(value: Any): Instant {
    return when (value) {
      is Instant -> value
      is java.time.Instant -> value.toKotlinInstant()
      is OffsetDateTime -> value.toInstant().toKotlinInstant()
      else -> error("$value is not Instant or OffsetDateTime!")
    }
  }

  override fun notNullValueToDB(value: Any): Any {
    if (value !is Instant) {
      error("$value is not a Instant!")
    }
    if (!currentDBSupportsTimeZone()) {
      // タイムゾーン情報を付けられないMySQLのようなDBの場合には強制的にタイムゾーンをUTCとして扱う
      return value.toLocalDateTime(TimeZone.UTC).toJavaLocalDateTime()
    }

    // see here for pgsql: https://jdbc.postgresql.org/documentation/query/#table51-supportedjava-8-date-and-time-classes
    return value.toJavaInstant().atOffset(ZoneOffset.UTC)
  }

  override fun readObject(rs: ResultSet, index: Int): Any? {
    return rs.getObject(index, OffsetDateTime::class.java)
  }

  private fun currentDBSupportsTimeZone(): Boolean {
    return arrayOf(
      PostgreSQLDialect.dialectName, H2Dialect.dialectName, OracleDialect.dialectName, SQLServerDialect.dialectName
    ).contains(
      currentDialect.name
    )
  }

  override fun sqlType(): String {
    return when (currentDialect.name) {
      PostgreSQLDialect.dialectName, H2Dialect.dialectName, OracleDialect.dialectName -> "TIMESTAMP WITH TIME ZONE"
      SQLServerDialect.dialectName -> "DATETIMEOFFSET"
      else -> "DATETIME"
    }
  }
}

fun Table.datetimeWithTZ(name: String): Column<Instant> = registerColumn(name, DataTimeWithTimeZoneColumnType())
