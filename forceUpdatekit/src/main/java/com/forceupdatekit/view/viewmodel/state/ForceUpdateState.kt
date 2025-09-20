package com.forceupdatekit.view.viewmodel.state

import com.forceupdatekit.service.model.CheckUpdateResponse
import com.sepanta.errorhandler.ApiError

sealed class ForceUpdateState {
    object Initial : ForceUpdateState()
    object NoUpdate : ForceUpdateState()
    data class UpdateError(val data: ApiError<*>?) : ForceUpdateState()

    object Update : ForceUpdateState()

    data class ShowView(val data: CheckUpdateResponse?) : ForceUpdateState()
    data class ShowViewError(val data: ApiError<*>?) : ForceUpdateState()
    object SkipError : ForceUpdateState()


}

