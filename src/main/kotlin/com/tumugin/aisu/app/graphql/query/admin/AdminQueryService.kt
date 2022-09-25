package com.tumugin.aisu.app.graphql.query.admin

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import graphql.schema.DataFetchingEnvironment

class AdminQueryService : Query {
  fun admin(dfe: DataFetchingEnvironment): AdminQueryServices {
    val context = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (context.adminUserAuthSession == null) {
      throw NotAuthorizedException()
    }
    return AdminQueryServices()
  }

  class AdminQueryServices() {
    fun adminUserAuth(): AdminUserAuthQueryService {
      return AdminUserAuthQueryService()
    }
  }
}
