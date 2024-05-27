package com.tumugin.aisu.infra.repository.exposed

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone
import java.time.OffsetDateTime

abstract class ExposedTimestampIdTable(name: String = "", columnName: String = "id") : LongIdTable(name, columnName) {
  val createdAt = timestampWithTimeZone("created_at").clientDefault { OffsetDateTime.now() }
  val updatedAt = timestampWithTimeZone("updated_at").clientDefault { OffsetDateTime.now() }
}

abstract class ExposedTimestampIdEntity(id: EntityID<Long>, table: ExposedTimestampIdTable) : LongEntity(id) {
  val createdAt by table.createdAt
  var updatedAt by table.updatedAt
}

abstract class ExposedTimestampIdEntityClass<E : ExposedTimestampIdEntity>(table: ExposedTimestampIdTable) :
  LongEntityClass<E>(table) {
  init {
    EntityHook.subscribe { action ->
      if (action.changeType == EntityChangeType.Updated) {
        action.toEntity(this)?.updatedAt = OffsetDateTime.now()
      }
    }
  }
}
