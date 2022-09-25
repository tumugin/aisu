package com.tumugin.aisu.testing.app.graphql.mutation.admin

import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AdminUserLogin
import com.tumugin.aisu.testing.graphql.client.inputs.AdminUserLoginParamsInput
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.plugins.cookies.*
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AdminUserLoginTest : BaseKtorTest() {
  lateinit var adminUser: AdminUser

  @BeforeEach
  fun beforeEach() = runTest {
    adminUser = AdminUserSeeder().seedAdminUser()
  }

  @Test
  fun testCanLogin() = testAisuApplication {
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

  @Test
  fun testCannotLoginWithWrongID() = testAisuApplication {
    val client = createClient {
      install(HttpCookies)
    }
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AdminUserLogin(
        AdminUserLogin.Variables(
          AdminUserLoginParamsInput(
            email = adminUser.adminUserEmail.value, password = "aoisuzuuuuuu"
          )
        )
      )
    )
    val cookies = client.cookies("http://localhost/")
    Assertions.assertNotNull(result.errors)
    Assertions.assertNull(result.data?.adminUserAuth?.adminUserLogin)
    Assertions.assertNull(cookies.firstOrNull { c -> c.name == "ADMIN_USER_AUTH" })
  }
}
