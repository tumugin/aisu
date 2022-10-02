package com.tumugin.aisu.testing.app.graphql.mutation.admin.adminUser

import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserRawPassword
import com.tumugin.aisu.domain.adminUser.AdminUserRepository
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.UpdateAdminUserPassword
import com.tumugin.aisu.testing.graphql.client.inputs.UpdateAdminUserPasswordParamsInput
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class UpdateAdminUserPasswordTest : BaseKtorTest() {
  lateinit var adminUser: AdminUser
  private val adminUserRepository by inject<AdminUserRepository>()

  @BeforeEach
  fun beforeEach() = runTest {
    adminUser = AdminUserSeeder().seedAdminUser()
  }

  @Test
  fun testUpdateAdminUserPassword() = testAisuApplication {
    val targetAdminUser = AdminUserSeeder().seedNonDuplicateAdminUser()
    val cookieValue = adminLoginAndGetCookieValue(this, adminUser)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      UpdateAdminUserPassword(
        UpdateAdminUserPassword.Variables(
          targetAdminUser.adminUserId.value.toString(), UpdateAdminUserPasswordParamsInput(
            "aoisuzz1234567890"
          )
        )
      )
    ) {
      header("Cookie", cookieValue)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.admin?.adminUser?.updateAdminUserPassword)

    val updatedTargetAdminUser = adminUserRepository.getAdminUserById(targetAdminUser.adminUserId)

    Assertions.assertTrue(
      updatedTargetAdminUser?.adminUserPassword?.isValid(AdminUserRawPassword("aoisuzz1234567890"))!!
    )
  }
}
