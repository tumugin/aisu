package com.tumugin.aisu.testing.app.graphql.query.admin

import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetAdminUser
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetAdminUserTest : BaseKtorTest() {
  @Test
  fun testGetAdminUser() = testAisuApplication {
    val adminUser = AdminUserSeeder().seedAdminUser()
    val cookie = adminLoginAndGetCookieValue(this, adminUser)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetAdminUser(GetAdminUser.Variables(adminUser.adminUserId.value.toString()))
    ) {
      header("Cookie", cookie)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.admin?.adminUsers?.getAdminUser)
  }
}
