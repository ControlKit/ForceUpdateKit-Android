package com.forceupdatekit.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.ForceUpdateApi
import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.service.model.toDomain
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ForceUpdateViewModel(
    private val api: ForceUpdateApi,
) : ViewModel() {
    private var config: ForceUpdateServiceConfig? = null
    fun setConfig(config: ForceUpdateServiceConfig) {
        this.config = config
        getData()
    }

    private val _mutableState = MutableStateFlow<ForceUpdateState>(ForceUpdateState.Initial)
    val state: StateFlow<ForceUpdateState> = _mutableState.asStateFlow()

    fun tryAgain() {
        clearState()
        getData()
    }

    fun clearState() {
        _mutableState.value = ForceUpdateState.Initial
    }

    fun getData() {
        if (state.value != ForceUpdateState.Initial || config == null) return
        viewModelScope.launch {
            val data = api.getForceUpdateData(
                config!!.route,
                config!!.appId,
                config!!.version,
                config!!.deviceId,
                config!!.sdkVersion
            )
            when (data) {
                is NetworkResult.Success -> {
                    if (data.value != null) {
                        _mutableState.value = ForceUpdateState.Update(data.value.toDomain())
                    } else {
                        _mutableState.value = ForceUpdateState.NoUpdate
                    }
                }

                is NetworkResult.Error -> {
                    if (config!!.skipException) {
                        _mutableState.value = ForceUpdateState.SkipError
                    } else {
                        _mutableState.value = ForceUpdateState.Error(data.error)

                    }
                }
            }
        }


    }

    private val _openDialog = MutableStateFlow(false)
    val openDialog: StateFlow<Boolean> = _openDialog.asStateFlow()

    fun showDialog() {
        _openDialog.value = true
    }

    fun dismissDialog() {
        _openDialog.value = false
        triggerForceUpdate()
    }


    private val _forceUpdateEvent = Channel<Unit>(Channel.BUFFERED)
    val forceUpdateEvent = _forceUpdateEvent.receiveAsFlow()

    fun triggerForceUpdate() {
        viewModelScope.launch {
            _forceUpdateEvent.send(Unit)
        }
    }
}