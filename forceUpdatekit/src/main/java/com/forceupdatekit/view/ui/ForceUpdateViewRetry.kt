package com.forceupdatekit.view.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.forceupdatekit.R
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.theme.Black100
import com.forceupdatekit.theme.Blue60
import com.forceupdatekit.theme.Typography
import com.forceupdatekit.theme.White100
import com.forceupdatekit.view.config.ForceUpdateViewConfig


@Composable
fun RetryView(config: ForceUpdateServiceConfig, tryAgain: (() -> Unit)? = null) {
    val openDialog = remember { mutableStateOf(false) }
    if (openDialog.value) return
    Dialog(
        onDismissRequest = { openDialog.value = true },

        properties = DialogProperties(
            usePlatformDefaultWidth = false, dismissOnClickOutside = false,
            dismissOnBackPress = false,
        )
    ) {
        Surface(
            modifier = config.viewConfig.popupViewLayoutModifier ?: Modifier
                .fillMaxSize(),
            color = config.viewConfig.popupViewBackGroundColor ?: White100

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageView(config.viewConfig)
                Title(config.viewConfig)
                if (config.viewConfig.retryViewSpace != null) config.viewConfig.retryViewSpace else Spacer(
                    modifier = Modifier.weight(
                        1.0f
                    )
                )

                ButtonTryAgain(config.viewConfig, tryAgain, openDialog)
                ButtonCancel(config, openDialog)
            }
        }

    }

}


@Composable
private fun ImageView(config: ForceUpdateViewConfig) {
    Surface(
        modifier = config.retryViewImageLayoutModifier ?: Modifier
            .padding(top = 40.dp)
            .width(100.dp)
            .height(100.dp),
        color = Color.Transparent,

        ) {
        if (config.retryViewImageView != null) config.retryViewImageView?.let { it() } else Icon(
            painter = painterResource(
                id = config.retryViewUpdateImageDrawble ?: R.drawable.no_wifi
            ),
            contentDescription = null,
            tint = config.retryViewImageColor
        )
    }

}

@Composable
private fun Title(
    config: ForceUpdateViewConfig,

    ) {
    Surface(
        modifier = config.retryViewTitleLayoutModifier ?: Modifier
            .padding(
                top = 60.dp,
                end = 15.dp,
                start = 15.dp
            )
            .wrapContentSize(),
        color = Color.Transparent,

        ) {
        config.retryViewTitleView ?: Text(
            text = config.retryViewTitle,
            style = Typography.titleLarge,
            color = config.retryViewTitleColor ?: Black100

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ButtonTryAgain(
    config: ForceUpdateViewConfig,
    tryAgain: (() -> Unit)?,
    openDialog: MutableState<Boolean>,
) {
    Surface(
        modifier = config.retryViewTryAgainButtonLayoutModifier ?: Modifier
            .padding(top = 60.dp)
            .wrapContentSize(),
        color = Color.Transparent,
        onClick = {
            openDialog.value = true

            tryAgain?.invoke()

        }

    ) {

        if (config.retryViewTryAgainButtonView != null) config.retryViewTryAgainButtonView?.let { it() } else ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(config.retryViewTryAgainButtonCornerRadius ?: 18.dp),

            colors = CardDefaults.cardColors(
                containerColor = config.retryViewTryAgainButtonColor ?: Blue60,

                ),
            modifier = Modifier
                .size(height = 48.dp, width = 182.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = config.retryViewTryAgainButtonTitle,
                    style = Typography.titleMedium
                )
            }
        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ButtonCancel(
    config: ForceUpdateServiceConfig,
    openDialog: MutableState<Boolean>,
) {
    val viewConfig = config.viewConfig
    Surface(
        modifier = viewConfig.retryViewCancelButtonLayoutModifier ?: Modifier
            .padding(
                top = 15.dp,
                bottom = 48.dp
            )
            .wrapContentSize(),
        color = Color.Transparent,
        onClick = {
            viewConfig.retryViewCancelButton?.invoke()
            if (!config.canDismissRetryView) return@Surface
            openDialog.value = true
        }

    ) {

        if (viewConfig.retryViewCancelButtonView != null) viewConfig.retryViewCancelButtonView?.let { it() } else ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(viewConfig.retryViewCancelButtonCornerRadius ?: 18.dp),

            colors = CardDefaults.cardColors(
                containerColor = viewConfig.retryViewCancelButtonColor ?: Blue60,

                ),
            modifier = Modifier
                .size(height = 48.dp, width = 182.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = viewConfig.retryViewCancelButtonTitle,
                    style = Typography.titleMedium
                )
            }
        }
    }

}


