package com.tumugin.aisu.app.graphql

import graphql.ExceptionWhileDataFetching
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult

class AisuDataFetcherExceptionHandler : DataFetcherExceptionHandler {
  override fun onException(handlerParameters: DataFetcherExceptionHandlerParameters?): DataFetcherExceptionHandlerResult {
    throw NotImplementedError()
    // TODO: GraphQLErrorをここで作る
    val error = null
    DataFetcherExceptionHandlerResult.newResult().error(error).build()
  }
}
