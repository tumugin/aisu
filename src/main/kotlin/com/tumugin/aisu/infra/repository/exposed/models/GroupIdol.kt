package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object GroupIdols : ExposedTimestampIdTable("group_idols") {
  val group = reference("group_id", Groups, onDelete = ReferenceOption.CASCADE)
  val idol = reference("idol_id", Idols, onDelete = ReferenceOption.CASCADE)
}

class GroupIdol(id: EntityID<Long>) : ExposedTimestampIdEntity(id, GroupIdols) {
  companion object : ExposedTimestampIdEntityClass<GroupIdol>(GroupIdols)

  var group by Group referencedOn GroupIdols.group
  var idol by Idol referencedOn GroupIdols.idol
}
