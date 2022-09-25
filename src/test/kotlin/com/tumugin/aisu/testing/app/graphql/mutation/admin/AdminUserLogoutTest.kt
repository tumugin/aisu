package com.tumugin.aisu.testing.app.graphql.mutation.admin

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.AdminUserLogout
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AdminUserLogoutTest : BaseKtorTest() {
  @Test
  fun testCanLogout() = testAisuApplication {
    val adminUser = AdminUserSeeder().seedAdminUser()
    val cookieValue = adminLoginAndGetCookieValue(this, adminUser)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      AdminUserLogout()
    ) {
      header("Cookie", cookieValue)
    }
    Assertions.assertNull(result.errors)
  }
}
