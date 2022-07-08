package com.tumugin.aisu.testing.usecase.admin.idol

import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.GroupSeeder
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.admin.group.WriteGroupAdmin
import com.tumugin.aisu.usecase.admin.idol.GetIdolAdmin
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetGroupsOfIdolTest : BaseDatabaseTest() {
  private val getIdolAdmin = GetIdolAdmin()
  private val writeGroupAdmin = WriteGroupAdmin()
  private lateinit var user: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE", "PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE", "OPERATION_DELETED"])
  fun testGetGroupsOfIdol(status: String) = runTest {
    val group = GroupSeeder().seedGroup(user.userId, groupStatus = GroupStatus.valueOf(status))
    GroupSeeder().seedGroup(userTwo.userId, groupStatus = GroupStatus.valueOf(status))
    val idol = IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.valueOf(status))
    IdolSeeder().seedIdol(userTwo.userId, idolStatus = IdolStatus.valueOf(status))

    writeGroupAdmin.addIdolToGroup(group, idol)

    Assertions.assertEquals(
      listOf(group),
      getIdolAdmin.getGroupsOfIdol(idol.idolId)
    )
  }
}
