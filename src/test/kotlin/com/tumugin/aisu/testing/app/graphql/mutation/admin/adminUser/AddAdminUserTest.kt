package com.tumugin.aisu.testing.app.graphql.mutation.admin.adminUser

import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AddAdminUser
import com.tumugin.aisu.testing.graphql.client.inputs.AddAdminUserParamsInput
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class AddAdminUserTest : BaseKtorTest() {
  private lateinit var adminUser: AdminUser
  private val adminUserRepository by inject<AdminUserRepository>()

  @BeforeEach
  fun beforeEach() = runTest {
    adminUser = AdminUserSeeder().seedAdminUser()
  }

  @Test
  fun testAddUser() = testAisuApplication {
    val cookieValue = adminLoginAndGetCookieValue(this, adminUser)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AddAdminUser(
        AddAdminUser.Variables(
          AddAdminUserParamsInput(
            email = "test@example.com", name = "あおいすず", password = "aoisuzz_12_07"
          )
        )
      )
    ) {
      header("Cookie", cookieValue)
    }
    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.admin?.adminUser?.addAdminUser)

    val addedAdminUserId = AdminUserId(
      result.data?.admin?.adminUser?.addAdminUser?.adminUserId?.toLong() ?: throw Exception("unexpected value.")
    )

    Assertions.assertNotNull(
      adminUserRepository.getAdminUserById(addedAdminUserId)
    )
  }
}
