package com.tumugin.aisu.app.graphql

import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import graphql.language.SourceLocation
import java.util.concurrent.CompletableFuture

class AisuDataFetcherExceptionHandler : DataFetcherExceptionHandler {
  override fun handleException(handlerParameters: DataFetcherExceptionHandlerParameters): CompletableFuture<DataFetcherExceptionHandlerResult> {
    val error = AisuGraphQLException(
      if (GraphQLErrorTypes.isSupportedType(handlerParameters.exception)) {
        handlerParameters.exception.message ?: "Exception while fetching data."
      } else {
        "Exception while fetching data."
      }, handlerParameters.sourceLocation, GraphQLErrorTypes.fromException(handlerParameters.exception)
    )
    val result = DataFetcherExceptionHandlerResult.newResult().error(error).build()
    return CompletableFuture.completedFuture(result)
  }

  class AisuGraphQLException(
    private val baseMessage: String,
    private val baseLocation: SourceLocation,
    private val baseErrorClassification: ErrorClassification
  ) : GraphQLError {
    override fun getMessage(): String {
      return this.baseMessage
    }

    override fun getLocations(): MutableList<SourceLocation> {
      return mutableListOf(this.baseLocation)
    }

    override fun getErrorType(): ErrorClassification {
      return this.baseErrorClassification
    }
  }
}
