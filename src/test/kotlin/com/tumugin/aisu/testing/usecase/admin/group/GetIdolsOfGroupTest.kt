package com.tumugin.aisu.testing.usecase.admin.group

import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupIdolSeeder
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.group.GetGroupAdmin
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetIdolsOfGroupTest : BaseDatabaseTest() {
  private val getGroupAdmin = GetGroupAdmin()
  private lateinit var userOne: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    userOne = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE", "PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE"])
  fun testGetIdolsOfGroupPublic(status: String) = runTest {
    val createdGroup = GroupSeeder().seedGroup(userOne.userId, groupStatus = GroupStatus.valueOf(status))
    val idolOne = IdolSeeder().seedIdol(userOne.userId, idolStatus = IdolStatus.valueOf(status))
    GroupIdolSeeder().seedGroupIdol(createdGroup.groupId, idolOne.idolId)
    val idolTwo = IdolSeeder().seedIdol(userOne.userId, idolStatus = IdolStatus.valueOf(status))
    GroupIdolSeeder().seedGroupIdol(createdGroup.groupId, idolTwo.idolId)

    val retrievedIdols = getGroupAdmin.getIdolsOfGroup(createdGroup)
    assertEquals(listOf(idolOne, idolTwo), retrievedIdols)
  }

  @Test
  fun testGetIdolsOfGroupOperationDeleted() = runTest {
    val createdGroup = GroupSeeder().seedGroup(userTwo.userId, groupStatus = GroupStatus.OPERATION_DELETED)
    val idolOne = IdolSeeder().seedIdol(userOne.userId, idolStatus = IdolStatus.OPERATION_DELETED)
    GroupIdolSeeder().seedGroupIdol(createdGroup.groupId, idolOne.idolId)
    val idolTwo = IdolSeeder().seedIdol(userOne.userId, idolStatus = IdolStatus.OPERATION_DELETED)
    GroupIdolSeeder().seedGroupIdol(createdGroup.groupId, idolTwo.idolId)

    assertEquals(listOf(idolOne, idolTwo), getGroupAdmin.getIdolsOfGroup(createdGroup))
  }
}
