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
  private val utcZoneOffset = ZoneOffset.UTC
  private val dateTimeStringFormatter =
    DateTimeFormatter.ISO_LOCAL_DATE_TIME.withLocale(Locale.ROOT).withZone(utcZoneOffset)

  override val hasTimePart: Boolean = true
  override fun sqlType(): String = currentDialect.dataTypeProvider.dateTimeType()

  override fun nonNullValueToString(value: Any): String {
    if (value !is Instant)
      error("$value is not a Instant!")

    return "'${dateTimeStringFormatter.format(value.toJavaInstant())}'"
  }

  override fun valueFromDB(value: Any): Instant {
    if (value is Instant) {
      return value
    }

    if (value is LocalDateTime) {
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
