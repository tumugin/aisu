package com.tumugin.aisu.testing.infra.repository

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.FavoriteGroupSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import kotlinx.coroutines.test.runTest
import org.koin.core.component.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FavoriteGroupRepositoryTest : BaseDatabaseTest() {
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()

  @BeforeTest
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
}
