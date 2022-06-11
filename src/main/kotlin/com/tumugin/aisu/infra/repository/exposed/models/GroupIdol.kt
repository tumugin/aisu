package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object GroupIdols : ExposedTimestampIdTable("group_idols") {
  val group = reference("group_id", Groups, onDelete = ReferenceOption.CASCADE)
  val idol = reference("idol_id", Idols, onDelete = ReferenceOption.CASCADE)
}
