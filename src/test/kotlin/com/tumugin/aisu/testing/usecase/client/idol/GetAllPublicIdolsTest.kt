package com.tumugin.aisu.testing.usecase.client.idol

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.idol.Idol
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

class GetAllPublicIdolsTest : BaseDatabaseTest() {
  private val getIdol = GetIdol()
  private lateinit var user: User
  private lateinit var userTwo: User

  @BeforeEach
  fun seed() = runTest {
    user = UserSeeder().seedNonDuplicateUser()
    userTwo = UserSeeder().seedNonDuplicateUser()
  }

  @ParameterizedTest
  @ValueSource(strings = ["PUBLIC_ACTIVE", "PUBLIC_NOT_ACTIVE"])
  fun testGetAllPublicIdols(status: String) = runTest {
    val publicIdol = IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.valueOf(status))
    val result = getIdol.getAllPublicIdols(PaginatorParam(1, 100))
    Assertions.assertEquals(listOf(publicIdol), result.result)
  }

  @ParameterizedTest
  @ValueSource(strings = ["PRIVATE_ACTIVE", "PRIVATE_NOT_ACTIVE"])
  fun testGetAllPublicIdolsReturnsEmpty(status: String) = runTest {
    IdolSeeder().seedIdol(user.userId, idolStatus = IdolStatus.valueOf(status))
    val result = getIdol.getAllPublicIdols(PaginatorParam(1, 100))
    Assertions.assertEquals(listOf<Idol>(), result.result)
  }
}
