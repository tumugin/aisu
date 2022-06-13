package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.FavoriteGroupSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class FavoriteGroupRepositoryTest : BaseDatabaseTest() {
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()

  @BeforeEach
  fun seed() = runTest {
    val user = UserSeeder().seedUser()
    (1..10).forEach { _ ->
      FavoriteGroupSeeder().seedFavoriteGroup(user.userId, GroupSeeder().seedGroup(user.userId).groupId)
    }
  }

  @Test
  fun testGetFavoriteGroupsByUserId() = runTest {
    val favoriteGroups = favoriteGroupRepository.getFavoriteGroupsByUserId(UserId(1))
    assertEquals(
      10,
      favoriteGroups.size
    )
  }

  @Test
  fun testDeleteFavoriteGroup() = runTest {
    favoriteGroupRepository.deleteFavoriteGroup(FavoriteGroupId(1))
    assertEquals(
      9,
      favoriteGroupRepository.getFavoriteGroupsByUserId(UserId(1)).size
    )
  }

  @Test
  fun testAddFavoriteGroup() = runTest {
    val user = UserSeeder().seedUser(
      userEmail = UserEmail("test_favorite_group_repository_test@example.com")
    )
    val group = GroupSeeder().seedGroup(user.userId)
    favoriteGroupRepository.addFavoriteGroup(
      user.userId,
      group.groupId
    )
    val result = favoriteGroupRepository.getFavoriteGroupsByUserId(user.userId)
    assertEquals(
      group.groupId.value,
      result[0].groupId.value
    )
    assertEquals(
      user.userId.value,
      result[0].userId.value
    )
  }
}
