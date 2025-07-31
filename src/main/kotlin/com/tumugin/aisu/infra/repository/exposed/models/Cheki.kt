@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.kotlin.datetime.timestampWithTimeZone
import kotlin.time.toKotlinInstant

object Chekis : ExposedTimestampIdTable("chekis") {
  val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
  val idol = reference("idol_id", Idols, onDelete = ReferenceOption.SET_NULL).nullable()
  val regulation = reference("regulation_id", Regulations, onDelete = ReferenceOption.SET_NULL).nullable()
  val quantity = integer("quantity")
  val shotAt = timestampWithTimeZone("shot_at")
}

class Cheki(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Chekis) {
  companion object : ExposedTimestampIdEntityClass<Cheki>(Chekis)

  var user by User referencedOn Chekis.user
  var idol by Idol optionalReferencedOn Chekis.idol
  var regulation by Regulation optionalReferencedOn Chekis.regulation
  var quantity by Chekis.quantity
  var shotAt by Chekis.shotAt

  fun toDomain(): com.tumugin.aisu.domain.cheki.Cheki {
    return Cheki(
      chekiId = ChekiId(this.id.value),
      userId = UserId(this.user.id.value),
      idolId = this.idol?.id?.value?.let { IdolId(it) },
      regulationId = this.regulation?.id?.value?.let { RegulationId(it) },
      regulation = this.regulation?.toDomain(),
      chekiQuantity = ChekiQuantity(this.quantity),
      chekiShotAt = ChekiShotAt(this.shotAt.toInstant().toKotlinInstant()),
      chekiCreatedAt = ChekiCreatedAt(this.createdAt.toInstant().toKotlinInstant()),
      chekiUpdatedAt = ChekiUpdatedAt(this.updatedAt.toInstant().toKotlinInstant())
    )
  }
}
