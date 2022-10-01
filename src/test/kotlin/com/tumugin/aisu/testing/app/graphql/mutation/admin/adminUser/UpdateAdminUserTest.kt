package com.tumugin.aisu.testing.app.graphql.mutation.admin.adminUser

import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UpdateAdminUser
import com.tumugin.aisu.testing.graphql.client.inputs.UpdateAdminUserParamsInput
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class UpdateAdminUserTest : BaseKtorTest() {
  lateinit var adminUser: AdminUser
  private val adminUserRepository by inject<AdminUserRepository>()

  @BeforeEach
  fun beforeEach() = runTest {
    adminUser = AdminUserSeeder().seedAdminUser()
  }

  @Test
  fun testUpdateUser() = testAisuApplication {
    val targetAdminUser = AdminUserSeeder().seedNonDuplicateAdminUser()
    val cookieValue = adminLoginAndGetCookieValue(this, adminUser)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      UpdateAdminUser(
        UpdateAdminUser.Variables(
          targetAdminUser.adminUserId.value.toString(), UpdateAdminUserParamsInput(
            "aoisuzz@example.com", "あおいすずちゃん！"
          )
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.admin?.adminUser?.updateAdminUser)

    val updatedTargetAdminUser = adminUserRepository.getAdminUserById(targetAdminUser.adminUserId)

    Assertions.assertEquals(
      "aoisuzz@example.com", updatedTargetAdminUser?.adminUserEmail?.value
    )
    Assertions.assertEquals(
      "あおいすずちゃん！", updatedTargetAdminUser?.adminUserName?.value
    )
  }
}
