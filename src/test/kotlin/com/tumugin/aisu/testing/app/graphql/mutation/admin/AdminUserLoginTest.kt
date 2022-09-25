package com.tumugin.aisu.testing.app.graphql.mutation.admin

import com.tumugin.aisu.domain.adminUser.AdminUserRawPassword
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AdminUserLogin
import com.tumugin.aisu.testing.graphql.client.inputs.AdminUserLoginParamsInput
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.plugins.cookies.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdminUserLoginTest : BaseKtorTest() {
  @Test
  fun testCanLogin() = testAisuApplication {
    val adminUser = AdminUserSeeder().seedAdminUser(
      adminUserRawPassword = AdminUserRawPassword("aoisuzu")
    )
    val client = createClient {
      install(HttpCookies)
    }
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AdminUserLogin(
        AdminUserLogin.Variables(
          AdminUserLoginParamsInput(
            email = adminUser.adminUserEmail.value, password = "aoisuzu"
          )
        )
      )
    )
    val cookies = client.cookies("http://localhost/")
    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.adminUserAuth?.adminUserLogin)
    Assertions.assertTrue(cookies.first { c -> c.name == "ADMIN_USER_AUTH" }.value.length > 50)
  }
}
