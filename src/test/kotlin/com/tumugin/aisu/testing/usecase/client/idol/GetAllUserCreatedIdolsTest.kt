package com.tumugin.aisu.testing.usecase.client.idol

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.idol.IdolStatus
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.testing.BaseDatabaseTest
import com.tumugin.aisu.testing.seeder.IdolSeeder
import com.tumugin.aisu.testing.seeder.UserSeeder
import com.tumugin.aisu.usecase.client.idol.GetIdol
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GetAllUserCreatedIdolsTest : BaseDatabaseTest() {
  private val getIdol = GetIdol()
  private lateinit var user: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE", "PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE", "OPERATION_DELETED"])
  fun testGetAllUserCreatedIdols(status: String) = runTest {
    val idol = IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.valueOf(status))
    IdolSeeder().seedIdol(userTwo.userId, idolStatus = IdolStatus.valueOf(status))
    val result = getIdol.getAllUserCreatedIdols(user.userId, PaginatorParam(1, 100))
    Assertions.assertEquals(listOf(idol), result.result)
  }
}
