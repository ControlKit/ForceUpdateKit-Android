package com.forceupdatekit.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.forceupdatekit.service.ForceUpdateApi

class ForceUpdateViewModelFactory(
    private val api: ForceUpdateApi
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForceUpdateViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForceUpdateViewModel(api) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}