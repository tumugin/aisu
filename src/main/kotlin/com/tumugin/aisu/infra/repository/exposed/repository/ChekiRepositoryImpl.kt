package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.DateFormatWithTZFunction
import com.tumugin.aisu.infra.repository.exposed.models.Chekis
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Cheki as ChekiModel

class ChekiRepositoryImpl : ChekiRepository {
  private val withModels =
    listOf(ChekiModel::user, ChekiModel::idol, ChekiModel::regulation).toTypedArray()
  private val idolWithModels =
    listOf(IdolModel::group, IdolModel::user).toTypedArray()

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
      ).with(*withModels)
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
      ).with(*withModels)
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
      val idols = IdolModel.forIds(idolIds).with(*idolWithModels)
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
      val yearConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "%Y",
        TimeZone.UTC,
        baseTimezone
      )
      val monthConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "%c",
        TimeZone.UTC,
        baseTimezone
      )
      val countResults = Chekis
        .slice(Chekis.idolId, yearConvertFunc, monthConvertFunc, Chekis.quantity.sum())
        .select(Chekis.userId.eq(userId.value))
        .groupBy(Chekis.idolId, yearConvertFunc, monthConvertFunc)
      val idolIds = countResults.mapNotNull { it[Chekis.idolId] }
      val idols = IdolModel.forIds(idolIds).with(*idolWithModels)
      countResults.map { row ->
        ChekiMonthIdolCount(
          idol = idols.find { idol -> idol.id.value === row[Chekis.idolId] }
            ?.let { v -> IdolRepositoryImpl.toDomain(v) },
          chekiCount = ChekiCount(row[Chekis.quantity.sum()] ?: 0),
          chekiShotAtMonth = ChekiShotAtMonth.fromString(row[yearConvertFunc], row[monthConvertFunc], baseTimezone)
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
    return toDomain(transaction {
      ChekiModel.new {
        this.userId = userId.value
        this.idolId = idolId.value
        this.regulationId = regulationId?.value
        this.quantity = chekiQuantity.value
        this.shotAt = chekiShotAt.value
      }
    })
  }

  override suspend fun updateCheki(
    chekiId: ChekiId,
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt
  ): Cheki {
    return toDomain(transaction {
      val model = ChekiModel[chekiId.value]
      model.userId = userId.value
      model.idolId = idolId.value
      model.regulationId = regulationId?.value
      model.quantity = chekiQuantity.value
      model.shotAt = chekiShotAt.value
      model
    })
  }

  override suspend fun deleteCheki(chekiId: ChekiId) {
    transaction {
      val model = ChekiModel[chekiId.value]
      model.delete()
    }
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
