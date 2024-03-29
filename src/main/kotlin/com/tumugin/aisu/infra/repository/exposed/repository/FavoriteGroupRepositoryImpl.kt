package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroup
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.FavoriteGroups
import kotlinx.coroutines.Dispatchers
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.tumugin.aisu.infra.repository.exposed.models.FavoriteGroup as FavoriteGroupModel

class FavoriteGroupRepositoryImpl : FavoriteGroupRepository {
  override suspend fun getFavoriteGroupsByUserId(userId: UserId): List<FavoriteGroup> {
    return newSuspendedTransaction(Dispatchers.IO) {
      FavoriteGroupModel.find(FavoriteGroups.user.eq(userId.value)).map { it.toDomain() }
    }
  }

  override suspend fun getFavoriteGroup(favoriteGroupId: FavoriteGroupId): FavoriteGroup? {
    return newSuspendedTransaction(Dispatchers.IO) {
      FavoriteGroupModel.findById(favoriteGroupId.value)?.toDomain()
    }
  }

  override suspend fun deleteFavoriteGroup(favoriteGroupId: FavoriteGroupId) {
    newSuspendedTransaction(Dispatchers.IO) {
      val model = FavoriteGroupModel[favoriteGroupId.value]
      model.delete()
    }
  }

  override suspend fun addFavoriteGroup(userId: UserId, groupId: GroupId): FavoriteGroup {
    return newSuspendedTransaction(Dispatchers.IO) {
      FavoriteGroupModel.new {
        this.user = UserModel[userId.value]
        this.group = GroupModel[groupId.value]
      }.toDomain()
    }
  }
}
