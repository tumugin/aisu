package com.tumugin.aisu.testing.app.graphql.query

import com.tumugin.aisu.domain.app.csrf.CSRFRepository
import com.tumugin.aisu.domain.app.csrf.CSRFToken
import com.tumugin.aisu.testing.BaseKtorTest
import com.tumugin.aisu.testing.graphql.client.GetCsrfToken
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class GetCsrfTokenTest : BaseKtorTest() {
  private val csrfRepository by inject<CSRFRepository>()

  @Test
  fun testGetCsrfToken() = testAisuApplication {
    val graphQLClient = createGraphQLKtorClient(client)
    val result = graphQLClient.execute(GetCsrfToken())
    Assertions.assertTrue(
      csrfRepository.validateTokenExists(
        CSRFToken(result.data!!.getCsrfToken)
      )
    )
  }
}
