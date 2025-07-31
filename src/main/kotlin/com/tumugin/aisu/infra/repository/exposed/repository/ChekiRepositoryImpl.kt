package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.DateFormatWithTZFunction
import com.tumugin.aisu.infra.repository.exposed.models.Chekis
import com.tumugin.aisu.infra.repository.exposed.models.Regulations
import kotlinx.coroutines.Dispatchers
import com.tumugin.aisu.infra.repository.exposed.models.Regulation as RegulationModel
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel
import kotlinx.datetime.TimeZone
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.between
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.times
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZoneOffset
import kotlin.time.toJavaInstant
import com.tumugin.aisu.infra.repository.exposed.models.Cheki as ChekiModel

class ChekiRepositoryImpl : ChekiRepository {
  private val withModels =
    listOf(ChekiModel::user, ChekiModel::idol, ChekiModel::regulation).toTypedArray()
  private val idolWithModels =
    listOf(IdolModel::user).toTypedArray()

  override suspend fun getCheki(chekiId: ChekiId): Cheki? {
    return newSuspendedTransaction(Dispatchers.IO) {
      ChekiModel.findById(chekiId.value)?.toDomain()
    }
  }

  override suspend fun getChekisByIds(chekiIds: List<ChekiId>): List<Cheki> {
    return newSuspendedTransaction(Dispatchers.IO) {
      ChekiModel.find { Chekis.id inList chekiIds.map { it.value } }.map { it.toDomain() }
    }
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRange(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return newSuspendedTransaction(Dispatchers.IO) {
      ChekiModel.find(
        Chekis.user.eq(userId.value) and Chekis.shotAt.between(
          chekiShotAtStart.value.toJavaInstant().atOffset(ZoneOffset.UTC),
          chekiShotEnd.value.toJavaInstant().atOffset(ZoneOffset.UTC)
        )
      )
        .sortedByDescending { it.shotAt }
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
    return newSuspendedTransaction(Dispatchers.IO) {
      ChekiModel.find(
        Chekis.user.eq(userId.value) and Chekis.shotAt.between(
          chekiShotAtStart.value.toJavaInstant().atOffset(ZoneOffset.UTC),
          chekiShotEnd.value.toJavaInstant().atOffset(ZoneOffset.UTC)
        ) and Chekis.idol.eq(idolId.value)
      )
        .sortedByDescending { it.shotAt }
        .with(*withModels)
        .map { it.toDomain() }
    }
  }

  override suspend fun getChekiIdolCountByUserId(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount> {
    return newSuspendedTransaction(Dispatchers.IO) {
      val countResults = Chekis
        .join(Regulations, JoinType.LEFT, Chekis.regulation, Regulations.id)
        .select(Chekis.idol, Chekis.quantity.sum(), Chekis.quantity.times(Regulations.unitPrice).sum())
        .where {
          Chekis.user.eq(userId.value) and Chekis.shotAt.between(
            chekiShotAtStart.value.toJavaInstant().atOffset(ZoneOffset.UTC),
            chekiShotEnd.value.toJavaInstant().atOffset(ZoneOffset.UTC)
          )
        }
        .groupBy(Chekis.idol)
        .orderBy(Chekis.idol)
      val idolIds = countResults.mapNotNull { it[Chekis.idol] }
      val idols = IdolModel.forEntityIds(idolIds).with(*idolWithModels)
      countResults.map { row ->
        ChekiIdolCount(
          idol = idols.find { idol -> idol.id.value == row[Chekis.idol]?.value }?.toDomain(),
          idolId = IdolId(row[Chekis.idol]!!.value),
          chekiCount = ChekiCount(row[Chekis.quantity.sum()] ?: 0),
          totalPrice = TotalPriceOfCheki(row[Chekis.quantity.times(Regulations.unitPrice).sum()] ?: 0)
        )
      }
    }
  }

  override suspend fun getChekiMonthIdolCountByUserIdAndIdol(
    userId: UserId,
    baseTimezone: TimeZone
  ): List<ChekiMonthIdolCount> {
    return newSuspendedTransaction(Dispatchers.IO) {
      val yearConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "yyyy",
        TimeZone.of("UTC"),
        baseTimezone
      )
      val monthConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "mm",
        TimeZone.of("UTC"),
        baseTimezone
      )
      val countResults = Chekis
        .select(Chekis.idol, yearConvertFunc, monthConvertFunc, Chekis.quantity.sum())
        .where(Chekis.user.eq(userId.value))
        .groupBy(Chekis.idol, yearConvertFunc, monthConvertFunc)
      val idolIds = countResults.mapNotNull { it[Chekis.idol] }
      val idols = IdolModel.forEntityIds(idolIds).with(*idolWithModels)
      countResults.map { row ->
        ChekiMonthIdolCount(
          idol = idols.find { idol -> idol.id.value == row[Chekis.idol]?.value }?.toDomain(),
          idolId = IdolId(row[Chekis.idol]!!.value),
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
    return newSuspendedTransaction(Dispatchers.IO) {
      ChekiModel.new {
        this.user = UserModel[userId.value]
        this.idol = IdolModel[idolId.value]
        this.regulation = regulationId?.value?.let { RegulationModel[it] }
        this.quantity = chekiQuantity.value
        this.shotAt = chekiShotAt.value.toJavaInstant().atOffset(ZoneOffset.UTC)
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
    return newSuspendedTransaction(Dispatchers.IO) {
      val model = ChekiModel[chekiId.value]
      model.user = UserModel[userId.value]
      model.idol = IdolModel[idolId.value]
      model.regulation = regulationId?.value?.let { RegulationModel[it] }
      model.quantity = chekiQuantity.value
      model.shotAt = chekiShotAt.value.toJavaInstant().atOffset(ZoneOffset.UTC)
      model.toDomain()
    }
  }

  override suspend fun deleteCheki(chekiId: ChekiId) {
    newSuspendedTransaction(Dispatchers.IO) {
      val model = ChekiModel[chekiId.value]
      model.delete()
    }
  }

  override suspend fun getChekiMonthIdolCountByUserIdAndIdolId(
    sessionUserId: UserId,
    idolId: IdolId,
    baseTimezone: TimeZone
  ): List<ChekiMonthCount> {
    return newSuspendedTransaction(Dispatchers.IO) {
      val yearConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "yyyy",
        TimeZone.of("UTC"),
        baseTimezone
      )
      val monthConvertFunc = DateFormatWithTZFunction(
        Chekis.shotAt,
        "mm",
        TimeZone.of("UTC"),
        baseTimezone
      )
      val countResults = Chekis
        .select(yearConvertFunc, monthConvertFunc, Chekis.quantity.sum())
        .where(Chekis.user.eq(sessionUserId.value) and Chekis.idol.eq(idolId.value))
        .groupBy(yearConvertFunc, monthConvertFunc)
      countResults.map { row ->
        ChekiMonthCount(
          count = ChekiCount(row[Chekis.quantity.sum()] ?: 0),
          month = ChekiShotAtMonth.fromString(row[yearConvertFunc], row[monthConvertFunc], baseTimezone)
        )
      }
    }
  }

  override suspend fun getChekiByUserIdAndPage(sessionUserId: UserId, page: PaginatorParam): PaginatorResult<Cheki> {
    return newSuspendedTransaction(Dispatchers.IO) {
      val count = ChekiModel.find { Chekis.user eq sessionUserId.value }.count()
      val chekis = ChekiModel.find { Chekis.user eq sessionUserId.value }
        .orderBy(Chekis.createdAt to SortOrder.DESC)
        .limit(page.limit.toInt())
        .offset(page.offset)
        .map { it.toDomain() }
      PaginatorResult(count = count, limit = page.limit, page = page.page, result = chekis)
    }
  }
}
