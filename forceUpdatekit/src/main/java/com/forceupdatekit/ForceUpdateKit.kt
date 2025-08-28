package com.forceupdatekit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModel
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.ApiService
import com.forceupdatekit.service.ForceUpdateApi
import com.forceupdatekit.service.RetrofitClientInstance
import com.forceupdatekit.view.config.ForceUpdateViewStyle
import com.forceupdatekit.view.ui.RetryView
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModelFactory
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState


class ForceUpdateKit(
    private var config: ForceUpdateServiceConfig = ForceUpdateServiceConfig(),
) {
    @Composable
    fun Configure(onDismiss: (() -> Unit)? = null) {
        if (RetrofitClientInstance.retrofitInstance == null) return

        val api = ForceUpdateApi(
            RetrofitClientInstance.retrofitInstance!!.create(ApiService::class.java)
        )

        val forceUpdateViewModel: ForceUpdateViewModel = viewModel(
            factory = ForceUpdateViewModelFactory(api)
        )
            forceUpdateViewModel.setConfig(config)
        val state = forceUpdateViewModel.state.collectAsState().value

        LaunchedEffect(Unit) {
            forceUpdateViewModel.forceUpdateEvent.collect {
                onDismiss?.invoke()
            }
        }

        when (state) {

            ForceUpdateState.Initial -> Unit

            ForceUpdateState.NoUpdate -> {
                config.viewConfig.noUpdateState?.invoke()
            }

            is ForceUpdateState.Update -> {
                state.data?.let {
                    ForceUpdateViewStyle.checkViewStyle(config.viewConfig.forceUpdateViewStyle)
                        .ShowView(config = config.viewConfig, it, forceUpdateViewModel)
                }

            }

            is ForceUpdateState.Error -> RetryView(config.viewConfig, tryAgain = {
                    forceUpdateViewModel.tryAgain()
                })


        }

    }


}


