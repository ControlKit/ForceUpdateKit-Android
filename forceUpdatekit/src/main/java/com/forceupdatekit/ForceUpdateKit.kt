package com.forceupdatekit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.contactsupportkit.view.viewModel.ForceUpdateViewModel
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.view.config.ForceUpdateViewStyle
import com.forceupdatekit.view.ui.RetryView
import com.forceupdatekit.view.viewModel.state.ForceUpdateState


class ForceUpdateKit(
    private var config: ForceUpdateServiceConfig = ForceUpdateServiceConfig(),
    private val forceUpdateViewModel: ForceUpdateViewModel = ForceUpdateViewModel()
) {


    @Composable
    fun Configure() {

        val state = forceUpdateViewModel.state.collectAsState().value

        when (state) {

            ForceUpdateState.Initial -> Unit

            ForceUpdateState.NoUpdate -> {
                config.viewConfig.noUpdateState?.invoke()
            }

            is ForceUpdateState.Update -> {
                state.data?.let {
                    ForceUpdateViewStyle.checkViewStyle(config.viewConfig.forceUpdateViewStyle)
                        .ShowView(config = config.viewConfig, it)

                }

            }

            is ForceUpdateState.Error -> {
                RetryView(config.viewConfig, tryAgain = {
                    forceUpdateViewModel.tryAgain()
                })
            }
        }

    }


}


