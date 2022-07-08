package com.tumugin.aisu.testing.usecase.admin.favoritegroup

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.favoritegroup.WriteFavoriteGroupAdmin
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class AddFavoriteGroupTest : BaseDatabaseTest() {
  private val writeFavoriteGroupAdmin = WriteFavoriteGroupAdmin()
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()
  lateinit var user: User
  lateinit var userTwo: User
  lateinit var group: Group

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    group = GroupSeeder().seedGroup(user.userId)
  }

  @Test
  fun testAddFavoriteGroup() = runTest {
    val favoriteGroup = writeFavoriteGroupAdmin.addFavoriteGroup(user.userId, group.groupId)
    val retrievedFavoriteGroup = favoriteGroupRepository.getFavoriteGroup(favoriteGroup.favoriteGroupId)

    Assertions.assertEquals(favoriteGroup.favoriteGroupId, retrievedFavoriteGroup?.favoriteGroupId)
    Assertions.assertEquals(favoriteGroup.userId, retrievedFavoriteGroup?.userId)
    Assertions.assertEquals(favoriteGroup.groupId, retrievedFavoriteGroup?.groupId)
  }
}
