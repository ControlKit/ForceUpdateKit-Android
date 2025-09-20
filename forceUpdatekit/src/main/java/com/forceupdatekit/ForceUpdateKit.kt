package com.forceupdatekit

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModel
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.ApiService
import com.forceupdatekit.service.ForceUpdateApi
import com.forceupdatekit.service.RetrofitClientInstance
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.view.config.ForceUpdateViewStyle
import com.forceupdatekit.view.ui.RetryView
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModelFactory
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState







class ForceUpdateKit(
    private var config: ForceUpdateServiceConfig ,
) {

    private var _viewModel: ForceUpdateViewModel? = null
    val viewModel: ForceUpdateViewModel
        get() = _viewModel ?: throw kotlin.IllegalStateException("ViewModel not initialized yet")

    init {
        setupViewModel()
    }

    private fun setupViewModel() {
        val retrofit = RetrofitClientInstance.getRetrofitInstance(
            config.timeOut,
            config.maxRetry,
            config.timeRetryThreadSleep
        ) ?: return

        val api = ForceUpdateApi(retrofit.create(ApiService::class.java))
        _viewModel = ForceUpdateViewModelFactory(api).create(ForceUpdateViewModel::class.java)
        _viewModel?.setConfig(config)
    }


    @Composable
    internal fun ConfigureComposable(
        onDismiss: (() -> Unit)? = null,
        onState: ((ForceUpdateState) -> Unit)? = null
    ) {
        if (_viewModel == null) return
        val state = _viewModel?.state?.collectAsState()?.value
        val response = remember { mutableStateOf<CheckUpdateResponse?>(null) }

        LaunchedEffect(Unit) {
            _viewModel?.forceUpdateEvent?.collect {
                onDismiss?.invoke()
            }
        }
        InitView(response)

        when (state) {

            ForceUpdateState.Initial -> onState?.invoke(ForceUpdateState.Initial)

            ForceUpdateState.NoUpdate -> {
                config.viewConfig.noUpdateState?.invoke()
                onState?.invoke(ForceUpdateState.NoUpdate)
            }
            ForceUpdateState.Update -> {
                onState?.invoke(ForceUpdateState.Update)
            }
            is ForceUpdateState.UpdateError -> {
                onState?.invoke(ForceUpdateState.UpdateError(state.data))
            }

            is ForceUpdateState.ShowView -> {
                state.data?.let {
                    response.value = it
                    onState?.invoke(ForceUpdateState.ShowView(it))
                    viewModel.showDialog()
                }
            }

            is ForceUpdateState.ShowViewError -> {
                RetryView(config, tryAgain = {
                    _viewModel?.tryAgain()
                })
                onState?.invoke(ForceUpdateState.ShowViewError(state.data))
            }

            ForceUpdateState.SkipError -> onState?.invoke(ForceUpdateState.SkipError)
            else -> Unit
        }
    }

    fun showView() {
        _viewModel?.getData()
    }
    @Composable
    private fun InitView(response: MutableState<CheckUpdateResponse?>) {
        response.value?.let {data->
            ForceUpdateViewStyle.checkViewStyle(config.viewConfig.forceUpdateViewStyle)
                .ShowView(config = config.viewConfig, data, viewModel)
        }
    }
}

@Composable
fun forceUpdateKitHost(
    config: ForceUpdateServiceConfig,
    onDismiss: (() -> Unit)? = null,
    onState: ((ForceUpdateState) -> Unit)? = null
): ForceUpdateKit {
    val kit = remember { ForceUpdateKit(config) }
    kit.ConfigureComposable(onState=onState, onDismiss = onDismiss)
    return kit
}





