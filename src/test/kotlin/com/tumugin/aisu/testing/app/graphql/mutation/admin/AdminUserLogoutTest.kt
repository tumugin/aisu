package com.tumugin.aisu.testing.app.graphql.mutation.admin

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AdminUserLogout
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdminUserLogoutTest : BaseKtorTest() {
  @Test
  fun testCanLogout() = testAisuApplication {
    val adminUser = AdminUserSeeder().seedAdminUser()
    val cookieValue = adminLoginAndGetCookieValue(this, adminUser)
    val client = createClient {
      install(HttpCookies)
    }
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AdminUserLogout()
    ) {
      header("Cookie", cookieValue)
    }
    val cookies = client.cookies("http://localhost/")
    Assertions.assertNull(result.errors)
    Assertions.assertNull(cookies.firstOrNull { c -> c.name == "ADMIN_USER_AUTH" })
  }
}
