package com.forceupdatekit.view.viewModel.state

import com.example.demoapp.models.CheckUpdateResponse
import com.joon.fm.data.source.remote.ApiError

sealed class ForceUpdateState {
    object Initial : ForceUpdateState()
    object NoUpdate : ForceUpdateState()
    data class Update(val data: CheckUpdateResponse?) : ForceUpdateState()
    data class Error(val data: ApiError?) : ForceUpdateState()


}

