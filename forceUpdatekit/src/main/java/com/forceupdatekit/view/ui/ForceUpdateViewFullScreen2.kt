package com.forceupdatekit.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.R
import com.forceupdatekit.theme.Black100
import com.forceupdatekit.theme.Black80
import com.forceupdatekit.theme.Blue60
import com.forceupdatekit.theme.Typography
import com.forceupdatekit.theme.White100
import com.forceupdatekit.util.Utils.openLink
import com.forceupdatekit.view.config.ForceUpdateViewConfig
import com.forceupdatekit.view.config.ForceUpdateViewContract
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModel

class ForceUpdateViewFullScreen2 : ForceUpdateViewContract {

    @Composable
    override fun ShowView(
        config: ForceUpdateViewConfig,
        response: CheckUpdateResponse,
        viewModel: ForceUpdateViewModel
    ) {
        val openDialog = viewModel.openDialog.collectAsState()
        if (openDialog.value) return
        Dialog(
            onDismissRequest = { viewModel.dismissDialog() },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = config.popupViewLayoutModifier ?: Modifier.fillMaxSize(),
                color = config.popupViewBackGroundColor ?: White100

            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageView(config, Modifier.weight(4f), response)
                    HeaderTitle(config, response, Modifier.weight(1f))
                    DescriptionTitle(config, response, Modifier.weight(4f))
                    Line(config)
                    ButtonUpdate(config, response, Modifier.weight(1f), viewModel)
                    VersionTitle(config, response, Modifier.weight(1f))
                }
            }

        }

    }


    @Composable
    private fun ImageView(
        config: ForceUpdateViewConfig, modifier: Modifier, response: CheckUpdateResponse
    ) {
        Box(
            modifier = config.imageLayoutModifier ?: modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter

        ) {
            config.imageView?.let { imageView ->
                response.iconUrl?.let { imageView(it) }
            } ?: if (config.imageDrawble != null) Image(
                painter = painterResource(
                    id = config.imageDrawble!!
                ),
                contentScale = config.contentScaleImageDrawble ?: ContentScale.Fit,
                contentDescription = null,
            ) else AsyncImage(
                model = response.iconUrl,
                contentDescription = null,
                placeholder = if (config.placeholderImageDrawble == null) null else painterResource(
                    config.placeholderImageDrawble!!
                ),
                contentScale = config.contentScaleImageDrawble ?: ContentScale.Fit,
                error = painterResource(
                    id = config.errorImageDrawble ?: R.drawable.space_ship_cloud
                ),
            )
        }

    }

    @Composable
    private fun HeaderTitle(
        config: ForceUpdateViewConfig, response: CheckUpdateResponse, modifier: Modifier
    ) {
        Box(
            modifier = config.headerTitleLayoutModifier ?: modifier
                .padding(
                    end = 15.dp, start = 15.dp
                )
                .fillMaxSize(), contentAlignment = Alignment.Center

        ) {

            config.headerTitleView?.let { textView ->
                textView((response.title ?: config.headerTitle))
            } ?: Text(
                text = response.title ?: config.headerTitle,
                style = Typography.titleMedium,
                color = config.headerTitleColor ?: Black100

            )
        }

    }

    @Composable
    private fun DescriptionTitle(
        config: ForceUpdateViewConfig, response: CheckUpdateResponse, modifier: Modifier
    ) {
        Box(
            modifier = config.descriptionTitleLayoutModifier ?: modifier
                .padding(
                    end = 15.dp, start = 15.dp
                )
                .fillMaxSize(), contentAlignment = Alignment.TopCenter

        ) {
            config.descriptionTitleView?.let { textView ->
                textView((response.description ?: config.descriptionTitle))
            } ?: Text(
                text = response.description ?: config.descriptionTitle,
                style = Typography.titleSmall,
                color = config.descriptionTitleColor ?: Typography.titleSmall.color

            )
        }

    }


    @Composable
    private fun Line(
        config: ForceUpdateViewConfig
    ) {
        Surface(
            modifier = config.lineLayoutModifier ?: Modifier
                .padding(
                    end = 25.dp, start = 25.dp
                )
                .wrapContentSize(),
            color = Color.Transparent,

            ) {
            config.lineView ?: HorizontalDivider(thickness = 1.dp, color = Black80)
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ButtonUpdate(
        config: ForceUpdateViewConfig,
        response: CheckUpdateResponse,
        modifier: Modifier,
        viewModel: ForceUpdateViewModel

    ) {
        val uriHandler = LocalUriHandler.current
        Box(
            config.buttonLayoutModifier ?: modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            val onClickAction: () -> Unit = {
                openLink(response.linkUrl, uriHandler)
                viewModel.dismissDialog()
                viewModel.clearState()
            }

            config.buttonView?.let { button ->
                button(onClickAction)
            } ?: Button(
                onClick = onClickAction,
                shape = RoundedCornerShape(config.buttonCornerRadius ?: 18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = config.buttonColor ?: Blue60
                ),
                modifier = Modifier.size(width = 182.dp, height = 48.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Text(
                    text = response.buttonTitle ?: ("Update New Version"),
                    style = Typography.titleMedium
                )
            }
        }

    }

    @Composable
    private fun VersionTitle(
        config: ForceUpdateViewConfig, response: CheckUpdateResponse, modifier: Modifier

    ) {
        Box(
            modifier = config.versionTitleLayoutModifier ?: modifier
                .padding(
                    end = 15.dp, start = 15.dp
                )
                .fillMaxSize(), contentAlignment = Alignment.TopCenter

        ) {
            config.versionTitleView?.let { textView ->
                textView((response.version ?: config.versionTitle))
            } ?: Text(
                text = response.version ?: config.versionTitle,
                style = Typography.titleSmall,
                color = config.versionTitleColor ?: Typography.titleSmall.color

            )
        }

    }

}