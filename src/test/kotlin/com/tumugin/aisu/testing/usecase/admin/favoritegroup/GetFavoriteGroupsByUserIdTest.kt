package com.tumugin.aisu.testing.usecase.admin.favoritegroup

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.FavoriteGroupSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.favoritegroup.GetFavoriteGroupAdmin
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetFavoriteGroupsByUserIdTest : BaseDatabaseTest() {
  private val getFavoriteGroupAdmin = GetFavoriteGroupAdmin()
  lateinit var user: User
  lateinit var userTwo: User
  lateinit var group: Group
  lateinit var nonVisibleGroup: Group

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
    group = GroupSeeder().seedGroup(user.userId)
    FavoriteGroupSeeder().seedFavoriteGroup(user.userId, group.groupId)

    // userからは見えないgroupをseedしてお気に入り登録しておく
    nonVisibleGroup = GroupSeeder().seedGroup(userTwo.userId, groupStatus = GroupStatus.PRIVATE_ACTIVE)
    FavoriteGroupSeeder().seedFavoriteGroup(user.userId, nonVisibleGroup.groupId)
  }

  @Test
  fun testGetFavoriteGroupsByUserId() = runTest {
    val favoriteGroups = getFavoriteGroupAdmin.getFavoriteGroupsByUserId(user.userId)
    Assertions.assertEquals(2, favoriteGroups.size)

    Assertions.assertEquals(user.userId, favoriteGroups[0].userId)
    Assertions.assertEquals(group.groupId, favoriteGroups[0].groupId)
    Assertions.assertEquals(group, favoriteGroups[0].group)

    // 見えなくなっているグループはgroupがnullになる
    Assertions.assertEquals(user.userId, favoriteGroups[1].userId)
    Assertions.assertEquals(nonVisibleGroup.groupId, favoriteGroups[1].groupId)
    Assertions.assertEquals(nonVisibleGroup, favoriteGroups[1].group)
  }
}
