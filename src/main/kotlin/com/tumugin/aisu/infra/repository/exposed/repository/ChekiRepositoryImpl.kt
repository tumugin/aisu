package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.DateFormatWithTZFunction
import com.tumugin.aisu.infra.repository.exposed.models.Chekis
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Cheki as ChekiModel

class ChekiRepositoryImpl : ChekiRepository {
  override suspend fun getCheki(chekiId: ChekiId): Cheki? {
    return transaction {
      ChekiModel.findById(chekiId.value)
    }?.let { toDomain(it) }
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRange(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return transaction {
      ChekiModel.find(
        Chekis.userId.eq(userId.value) and Chekis.shotAt.between(
          chekiShotAtStart.value,
          chekiShotEnd.value
        )
      )
    }.map { toDomain(it) }
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRangeAndIdolId(
    userId: UserId,
    idolId: IdolId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return transaction {
      ChekiModel.find(
        Chekis.userId.eq(userId.value) and Chekis.shotAt.between(
          chekiShotAtStart.value,
          chekiShotEnd.value
        ) and Chekis.idolId.eq(idolId.value)
      )
    }.map { toDomain(it) }
  }

  override suspend fun getChekiIdolCountByUserId(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount> {
    return transaction {
      val countResults = Chekis
        .slice(Chekis.idolId, Chekis.quantity.sum())
        .select {
          Chekis.userId.eq(userId.value) and Chekis.shotAt.between(
            chekiShotAtStart.value,
            chekiShotEnd.value
          )
        }
        .groupBy(Chekis.idolId)
      val idolIds = countResults.mapNotNull { it[Chekis.idolId] }
      val idols = IdolModel.forIds(idolIds)
      countResults.map { row ->
        ChekiIdolCount(
          idol = idols.find { idol -> idol.id.value === row[Chekis.idolId] }
            ?.let { v -> IdolRepositoryImpl.toDomain(v) },
          chekiCount = ChekiCount(row[Chekis.quantity.sum()] ?: 0)
        )
      }
    }
  }

  override suspend fun getChekiMonthIdolCountByUserIdAndIdol(
    userId: UserId,
    idolId: IdolId,
    baseTimezone: TimeZone
  ): List<ChekiMonthIdolCount> {
    return transaction {
      val monthConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "%Y-%c",
        TimeZone.UTC,
        baseTimezone
      )
      val countResults = Chekis
        .slice(Chekis.idolId, monthConvertFunc, Chekis.quantity.sum())
        .select(Chekis.userId.eq(userId.value))
        .groupBy(Chekis.idolId, monthConvertFunc)
      val idolIds = countResults.mapNotNull { it[Chekis.idolId] }
      val idols = IdolModel.forIds(idolIds)
      countResults.map { row ->
        ChekiMonthIdolCount(
          idol = idols.find { idol -> idol.id.value === row[Chekis.idolId] }
            ?.let { v -> IdolRepositoryImpl.toDomain(v) },
          chekiCount = ChekiCount(row[Chekis.quantity.sum()] ?: 0),
          chekiShotAtMonth = ChekiShotAtMonth.fromString(row[monthConvertFunc], baseTimezone)
        )
      }
    }
  }

  override suspend fun addCheki(
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt
  ): Cheki {
    TODO("Not yet implemented")
  }

  override suspend fun updateCheki(
    chekiId: ChekiId,
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt
  ): Cheki {
    TODO("Not yet implemented")
  }

  override suspend fun deleteCheki(chekiId: ChekiId) {
    TODO("Not yet implemented")
  }

  companion object {
    fun toDomain(rawModel: ChekiModel): Cheki {
      return Cheki(
        chekiId = ChekiId(rawModel.id.value),
        userId = UserId(rawModel.userId),
        user = UserRepositoryImpl.toDomain(rawModel.user),
        idolId = rawModel.idolId?.let { IdolId(it) },
        idol = rawModel.idol?.let { IdolRepositoryImpl.toDomain(it) },
        regulationId = rawModel.regulationId?.let { RegulationId(it) },
        regulation = rawModel.regulation?.let { RegulationRepositoryImpl.toDomain(it) },
        chekiQuantity = ChekiQuantity(rawModel.quantity),
        chekiShotAt = ChekiShotAt(rawModel.shotAt),
        chekiCreatedAt = ChekiCreatedAt(rawModel.createdAt),
        chekiUpdatedAt = ChekiUpdatedAt(rawModel.updatedAt)
      )
    }
  }
}
