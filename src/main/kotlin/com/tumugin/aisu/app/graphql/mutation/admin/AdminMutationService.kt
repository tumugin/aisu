package com.tumugin.aisu.app.graphql.mutation.admin

import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import graphql.schema.DataFetchingEnvironment

class AdminMutationService : Mutation {
  fun admin(dfe: DataFetchingEnvironment): AdminMutationServices {
    val context = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (context.adminUserAuthSession == null) {
      throw NotAuthorizedException()
    }
    return AdminMutationServices()
  }

  fun adminUserAuth(): AdminUserAuthMutationService {
    return AdminUserAuthMutationService()
  }

  class AdminMutationServices : Mutation {
    fun adminUser(): AdminUserMutationService {
      return AdminUserMutationService()
    }
  }
}
