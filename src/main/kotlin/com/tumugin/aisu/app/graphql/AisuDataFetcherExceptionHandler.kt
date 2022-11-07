package com.tumugin.aisu.app.graphql

import graphql.ErrorClassification
import graphql.GraphQLError
import graphql.execution.DataFetcherExceptionHandler
import graphql.execution.DataFetcherExceptionHandlerParameters
import graphql.execution.DataFetcherExceptionHandlerResult
import graphql.execution.ResultPath
import graphql.language.SourceLocation
import io.ktor.server.request.*
import io.ktor.util.logging.*
import java.util.concurrent.CompletableFuture

class AisuDataFetcherExceptionHandler : DataFetcherExceptionHandler {
  override fun handleException(handlerParameters: DataFetcherExceptionHandlerParameters): CompletableFuture<DataFetcherExceptionHandlerResult> {
    val isSupportedError = GraphQLErrorTypes.isSupportedType(handlerParameters.exception)

    val request =
      handlerParameters.dataFetchingEnvironment.graphQlContext.get<ApplicationRequest>(ApplicationRequest::class)
    if (!isSupportedError) {
      request.call.application.environment.log.error(handlerParameters.exception)
    }

    val error = AisuGraphQLException(
      if (isSupportedError) {
        handlerParameters.exception.message ?: "Exception while fetching data."
      } else {
        "Exception while fetching data."
      },
      handlerParameters.sourceLocation,
      GraphQLErrorTypes.fromException(handlerParameters.exception),
      handlerParameters.path
    )
    val result = DataFetcherExceptionHandlerResult.newResult().error(error).build()
    return CompletableFuture.completedFuture(result)
  }

  class AisuGraphQLException(
    private val baseMessage: String,
    private val baseLocation: SourceLocation,
    private val baseErrorClassification: GraphQLErrorTypes,
    private val basePath: ResultPath
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

    override fun getExtensions(): MutableMap<String, Any> {
      return mutableMapOf(
        "type" to this.baseErrorClassification.name,
      )
    }

    override fun getPath(): MutableList<Any> {
      return this.basePath.toList()
    }
  }
}
