package com.forceupdatekit.service.apiError

import com.joon.fm.core.base.baseUsecase.traceErrorException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class BaseUsecase<Type, in RequestModel> where Type : Any {

    abstract suspend fun run(requestModel: RequestModel): Type


    fun invoke(
        scope: CoroutineScope,
        requestModel: RequestModel,
        onResult: UseCaseResponse<Type>?
    ) {

        scope.launch(SupervisorJob()) {
            try {
                val result = run(requestModel)
                onResult?.onSuccess(result)
            } catch (e: CancellationException) {
                onResult?.onError(traceErrorException(e))

            } catch (e: Exception) {
                onResult?.onError(traceErrorException(e))
            }

        }
    }

}

