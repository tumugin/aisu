package com.tumugin.aisu.testing.usecase.admin.favoritegroup

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroup
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupRepository
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.FavoriteGroupSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.favoritegroup.WriteFavoriteGroupAdmin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class DeleteFavoriteGroupTest : BaseDatabaseTest() {
  private val writeFavoriteGroupAdmin = WriteFavoriteGroupAdmin()
  private val favoriteGroupRepository by inject<FavoriteGroupRepository>()
  lateinit var user: User
  lateinit var userTwo: User
  lateinit var favoriteGroup: FavoriteGroup

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    val group = GroupSeeder().seedGroup(user.userId)
    favoriteGroup = FavoriteGroupSeeder().seedFavoriteGroup(user.userId, group.groupId)
  }

  @Test
  fun testDeleteFavoriteGroup() = runTest {
    writeFavoriteGroupAdmin.deleteFavoriteGroup(favoriteGroup.favoriteGroupId)
    Assertions.assertNull(favoriteGroupRepository.getFavoriteGroup(favoriteGroup.favoriteGroupId))
  }

  @Test
  fun testDeleteFavoriteGroupWithOtherUser() {
    Assertions.assertDoesNotThrow {
      runBlocking {
        writeFavoriteGroupAdmin.deleteFavoriteGroup(favoriteGroup.favoriteGroupId)
      }
    }
  }

  @Test
  fun testDeleteFavoriteGroupWithNonExisting() {
    Assertions.assertThrows(NotFoundException::class.java) {
      runBlocking {
        writeFavoriteGroupAdmin.deleteFavoriteGroup(FavoriteGroupId(1000))
      }
    }
  }
}
