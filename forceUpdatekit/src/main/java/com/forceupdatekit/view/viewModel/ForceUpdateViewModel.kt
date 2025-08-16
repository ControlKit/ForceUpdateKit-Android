package com.contactsupportkit.view.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcreatelibrary.ui.view.data.ApiService
import com.example.testcreatelibrary.ui.view.data.RetrofitClientInstance
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.service.apiError.NetworkResult
import com.forceupdatekit.service.apiError.handleApi
import com.forceupdatekit.view.viewModel.state.ForceUpdateState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ForceUpdateViewModel : ViewModel() {
    private var config: ForceUpdateServiceConfig = ForceUpdateServiceConfig()
    val retrofit = RetrofitClientInstance.retrofitInstance
    val apiInterface = retrofit?.create(ApiService::class.java)

    init {
        getData()
    }

    private val _mutableState =
        MutableStateFlow<ForceUpdateState>(ForceUpdateState.Initial)
    val state: StateFlow<ForceUpdateState> =
        _mutableState.asStateFlow()

    fun tryAgain() {
        _mutableState.value = ForceUpdateState.Initial
        getData()
    }
    fun getData() {
        viewModelScope.launch {
            Log.i("Success222", "viewModelScope  ")

            val data = handleApi{
                apiInterface?.getData(
                    url = config.route,
//                    lastId = "uy",
                    appId = config.appId,
                    language = config.language,
                    version = config.version,
                    deviceId = "456456"
                )
            }


            when (data) {
                is NetworkResult.Success -> {
                    Log.i("Success222", "Success Success ${data.value}  ")

                    if (data.value != null) {

                        _mutableState.value = ForceUpdateState.Update(data.value)

                    } else {
                        _mutableState.value = ForceUpdateState.NoUpdate

                    }
                }

                is NetworkResult.Error -> {
                    Log.i("Success222", "Success Error ${data.error}  ")

                    _mutableState.value = ForceUpdateState.Error(data.error)
                }
            }
        }


    }

}