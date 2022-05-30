package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroup
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.FavoriteGroups
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.FavoriteGroup as FavoriteGroupModel

class FavoriteGroupRepositoryImpl : FavoriteGroupRepository {
  override suspend fun getFavoriteGroupsByUserId(userId: UserId): List<FavoriteGroup> {
    return transaction {
      FavoriteGroupModel.find(FavoriteGroups.userId eq userId.value).asIterable()
    }.map { toDomain(it) }
  }

  override suspend fun deleteFavoriteGroup(favoriteGroupId: FavoriteGroupId) {
    transaction {
      val model = FavoriteGroupModel[favoriteGroupId.value]
      model.delete()
    }
  }

  override suspend fun addFavoriteGroup(userId: UserId, groupId: GroupId): FavoriteGroup {
    return toDomain(transaction {
      FavoriteGroupModel.new {
        this.userId = userId.value
        this.groupId = groupId.value
      }
    })
  }

  companion object {
    fun toDomain(model: FavoriteGroupModel): FavoriteGroup {
      return FavoriteGroup(
        favoriteGroupId = FavoriteGroupId(model.id.value),
        userId = UserId(model.userId),
        groupId = GroupId(model.groupId)
      )
    }
  }
}
