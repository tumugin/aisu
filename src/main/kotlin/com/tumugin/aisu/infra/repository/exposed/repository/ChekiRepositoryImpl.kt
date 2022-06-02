package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.DateFormatWithTZFunction
import com.tumugin.aisu.infra.repository.exposed.models.Chekis
import com.tumugin.aisu.infra.repository.exposed.models.Regulation as RegulationModel
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
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
      ChekiModel.findById(chekiId.value)?.toDomain()
    }
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRange(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return transaction {
      ChekiModel.find(
        Chekis.user.eq(userId.value) and Chekis.shotAt.between(
          chekiShotAtStart.value,
          chekiShotEnd.value
        )
      )
        .with(*withModels)
        .map { it.toDomain() }
    }
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRangeAndIdolId(
    userId: UserId,
    idolId: IdolId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return transaction {
      ChekiModel.find(
        Chekis.user.eq(userId.value) and Chekis.shotAt.between(
          chekiShotAtStart.value,
          chekiShotEnd.value
        ) and Chekis.idol.eq(idolId.value)
      ).with(*withModels).map { it.toDomain() }
    }
  }

  override suspend fun getChekiIdolCountByUserId(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount> {
    return transaction {
      val countResults = Chekis
        .slice(Chekis.idol, Chekis.quantity.sum())
        .select {
          Chekis.user.eq(userId.value) and Chekis.shotAt.between(
            chekiShotAtStart.value,
            chekiShotEnd.value
          )
        }
        .groupBy(Chekis.idol)
      val idolIds = countResults.mapNotNull { it[Chekis.idol] }
      val idols = IdolModel.forEntityIds(idolIds).with(*idolWithModels)
      countResults.map { row ->
        ChekiIdolCount(
          idol = idols.find { idol -> idol.id === row[Chekis.idol] }
            ?.let { v -> v.toDomain() },
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
        .slice(Chekis.idol, yearConvertFunc, monthConvertFunc, Chekis.quantity.sum())
        .select(Chekis.user.eq(userId.value))
        .groupBy(Chekis.idol, yearConvertFunc, monthConvertFunc)
      val idolIds = countResults.mapNotNull { it[Chekis.idol] }
      val idols = IdolModel.forEntityIds(idolIds).with(*idolWithModels)
      countResults.map { row ->
        ChekiMonthIdolCount(
          idol = idols.find { idol -> idol.id === row[Chekis.idol] }
            ?.let { v -> v.toDomain() },
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
    return transaction {
      ChekiModel.new {
        this.user = UserModel[userId.value]
        this.idol = IdolModel[idolId.value]
        this.regulation = regulationId?.value?.let { RegulationModel[it] }
        this.quantity = chekiQuantity.value
        this.shotAt = chekiShotAt.value
      }.toDomain()
    }
  }

  override suspend fun updateCheki(
    chekiId: ChekiId,
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt
  ): Cheki {
    return transaction {
      val model = ChekiModel[chekiId.value]
      model.user = UserModel[userId.value]
      model.idol = IdolModel[idolId.value]
      model.regulation = regulationId?.value?.let { RegulationModel[it] }
      model.quantity = chekiQuantity.value
      model.shotAt = chekiShotAt.value
      model.toDomain()
    }
  }

  override suspend fun deleteCheki(chekiId: ChekiId) {
    transaction {
      val model = ChekiModel[chekiId.value]
      model.delete()
    }
  }
}
