package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.datetimeWithTZ
import com.tumugin.aisu.infra.repository.exposed.repository.IdolRepositoryImpl
import com.tumugin.aisu.infra.repository.exposed.repository.RegulationRepositoryImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Chekis : ExposedTimestampIdTable("chekis") {
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
  val user = reference("user_id", Users)
  val idolId = long("idol_id").references(Idols.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val idol = reference("idol_id", Idols).nullable()
  val regulationId = long("regulation_id").references(Regulations.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val regulation = reference("regulation_id", Regulations).nullable()
  val quantity = integer("quantity")
  val shotAt = datetimeWithTZ("shot_at")
}

class Cheki(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Chekis) {
  companion object : ExposedTimestampIdEntityClass<Cheki>(Chekis)

  var userId by Chekis.userId
  val user by User referencedOn Chekis.user
  var idolId by Chekis.idolId
  val idol by Idol optionalReferencedOn Chekis.idol
  var regulationId by Chekis.regulationId
  val regulation by Regulation optionalReferencedOn Chekis.regulation
  var quantity by Chekis.quantity
  var shotAt by Chekis.shotAt

  fun toDomain(): com.tumugin.aisu.domain.cheki.Cheki {
    return Cheki(
      chekiId = ChekiId(this.id.value),
      userId = UserId(this.userId),
      user = this.user.toDomain(),
      idolId = this.idolId?.let { IdolId(it) },
      idol = this.idol?.toDomain(),
      regulationId = this.regulationId?.let { RegulationId(it) },
      regulation = this.regulation?.toDomain(),
      chekiQuantity = ChekiQuantity(this.quantity),
      chekiShotAt = ChekiShotAt(this.shotAt),
      chekiCreatedAt = ChekiCreatedAt(this.createdAt),
      chekiUpdatedAt = ChekiUpdatedAt(this.updatedAt)
    )
  }
}
