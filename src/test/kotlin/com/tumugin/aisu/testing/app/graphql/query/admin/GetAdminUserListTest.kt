package com.tumugin.aisu.testing.app.graphql.query.admin

import com.tumugin.aisu.domain.adminUser.AdminUserName
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetAdminUserList
import com.tumugin.aisu.testing.seeder.AdminUserSeeder
import io.ktor.client.request.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GetAdminUserListTest : BaseKtorTest() {
  @Test
  fun testGetAdminUserList() = testAisuApplication {
    (1..20).forEach {
      AdminUserSeeder().seedNonDuplicateAdminUser(
        adminUserName = AdminUserName("test-name-${it}"),
      )
    }
    val adminUser = AdminUserSeeder().seedAdminUser()
    val cookie = adminLoginAndGetCookieValue(this, adminUser)
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(
      GetAdminUserList(GetAdminUserList.Variables(1))
    ) {
      header("Cookie", cookie)
    }

    Assertions.assertNull(result.errors)
    Assertions.assertNotNull(result.data?.admin?.adminUsers?.getAdminUserList)
    Assertions.assertEquals(3, result.data?.admin?.adminUsers?.getAdminUserList?.pageCount)
    Assertions.assertEquals(1, result.data?.admin?.adminUsers?.getAdminUserList?.currentPage)
    Assertions.assertEquals(21, result.data?.admin?.adminUsers?.getAdminUserList?.count)
  }
}
