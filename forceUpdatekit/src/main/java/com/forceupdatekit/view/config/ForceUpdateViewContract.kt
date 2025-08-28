package com.forceupdatekit.view.config

import androidx.compose.runtime.Composable
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModel

interface ForceUpdateViewContract {
    @Composable
    fun ShowView(config: ForceUpdateViewConfig,response: CheckUpdateResponse,viewModel: ForceUpdateViewModel)
}