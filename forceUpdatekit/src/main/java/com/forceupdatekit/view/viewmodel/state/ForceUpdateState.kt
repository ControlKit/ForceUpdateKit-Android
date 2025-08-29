package com.forceupdatekit.view.viewmodel.state

import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.service.apiError.ApiError

sealed class ForceUpdateState {
    object Initial : ForceUpdateState()
    object NoUpdate : ForceUpdateState()
    data class Update(val data: CheckUpdateResponse?) : ForceUpdateState()
    data class Error(val data: ApiError?) : ForceUpdateState()
    object SkipError : ForceUpdateState()


}

