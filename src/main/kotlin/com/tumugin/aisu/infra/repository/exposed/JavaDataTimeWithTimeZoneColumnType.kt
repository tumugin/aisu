package com.tumugin.aisu.infra.repository.exposed

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.IDateColumnType
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.vendors.currentDialect
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class JavaDataTimeWithTimeZoneColumnType : ColumnType(), IDateColumnType {
  private val dateTimeStringFormatter = DateTimeFormatter.ISO_INSTANT
  override val hasTimePart: Boolean = true
  override fun sqlType(): String = currentDialect.dataTypeProvider.dateTimeType()

  override fun nonNullValueToString(value: Any): String {
    if (value !is Instant) error("$value is not a Instant!")

    return "'${dateTimeStringFormatter.format(value.toJavaInstant())}'"
  }

  override fun valueFromDB(value: Any): Instant {
    if (value is Instant) {
      return value
    }

    if (value is java.time.Instant) {
      return value.toKotlinInstant()
    }

    if (value is LocalDateTime) {
      // タイムゾーンをサポートしないDBから来た時は強制的にUTCへ変換する
      return value.toKotlinLocalDateTime().toInstant(UtcOffset.ZERO)
    }

    error("$value is not Instant or LocalDateTime")
  }

  override fun notNullValueToDB(value: Any): Any {
    if (value !is Instant) {
      error("$value is not a Instant!")
    }

    return Timestamp.from(value.toJavaInstant())
  }
}

fun Table.datetimeWithTZ(name: String): Column<Instant> = registerColumn(name, JavaDataTimeWithTimeZoneColumnType())
