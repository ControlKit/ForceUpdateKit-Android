package com.forceupdatekit.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import coil.compose.AsyncImage
import com.forceupdatekit.service.model.CheckUpdateResponse
import com.forceupdatekit.R
import com.forceupdatekit.theme.Black100
import com.forceupdatekit.theme.Orange80
import com.forceupdatekit.theme.Typography
import com.forceupdatekit.util.Utils.openLink
import com.forceupdatekit.view.config.ForceUpdateViewConfig
import com.forceupdatekit.view.config.ForceUpdateViewContract
import com.forceupdatekit.view.viewmodel.ForceUpdateViewModel

class ForceUpdateViewPopover1 : ForceUpdateViewContract {

    @Composable
    override fun ShowView(
        config: ForceUpdateViewConfig,
        response: CheckUpdateResponse,
        viewModel: ForceUpdateViewModel
    ) {

        val openDialog = viewModel.openDialog.collectAsState()
        if (!openDialog.value) return
        Dialog(
            onDismissRequest = { viewModel.dismissDialog() }) {
            Surface(
                modifier = config.popupViewLayoutModifier ?: Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(config.popupViewCornerRadius ?: 15.dp),
                color = config.popupViewBackGroundColor ?: Black100
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ImageView(config, response)
                    HeaderTitle(config, response)
                    DescriptionTitle(config, response)
                    ButtonUpdate(config, response, viewModel)
                }
            }

        }

    }


    @Composable
    private fun ImageView(config: ForceUpdateViewConfig, response: CheckUpdateResponse) {
        Surface(
            modifier = config.imageLayoutModifier ?: Modifier
                .padding(top = 60.dp)
                .wrapContentWidth()
                .height(120.dp),

            color = Color.Transparent,

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
        config: ForceUpdateViewConfig,
        response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.headerTitleLayoutModifier ?: Modifier
                .padding(top = 30.dp, end = 15.dp, start = 15.dp)
                .wrapContentSize(),
            color = Color.Transparent,

            ) {
            config.headerTitleView?.let { textView ->
                textView((response.title ?: config.headerTitle))
            } ?: Text(
                text = response.title ?: config.headerTitle,
                style = Typography.titleMedium,
                color = config.headerTitleColor ?: Typography.titleMedium.color

            )
        }

    }

    @Composable
    private fun DescriptionTitle(
        config: ForceUpdateViewConfig,
        response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.descriptionTitleLayoutModifier ?: Modifier
                .padding(top = 20.dp, end = 15.dp, start = 15.dp)
                .wrapContentSize(),
            color = Color.Transparent,

            ) {
            config.descriptionTitleView?.let { textView ->
                textView((response.description ?: config.descriptionTitle))
            } ?: Text(
                text = response.description
                    ?: config.descriptionTitle,
                style = Typography.titleSmall,
                color = config.descriptionTitleColor ?: Typography.titleSmall.color

            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ButtonUpdate(
        config: ForceUpdateViewConfig,
        response: CheckUpdateResponse,
        viewModel: ForceUpdateViewModel
    ) {
        val uriHandler = LocalUriHandler.current


        val onClickAction: () -> Unit = {
            openLink(response.linkUrl, uriHandler)
            viewModel.submit()
        }
        config.buttonView?.let { button ->
            button(onClickAction)
        } ?: Button(
            onClick = onClickAction,
            shape = RoundedCornerShape(config.buttonCornerRadius ?: 18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = config.buttonColor ?: Orange80
            ),
            modifier = Modifier
                .padding(top = 40.dp, bottom = 40.dp)
                .size(width = 182.dp, height = 48.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp)
        ) {
            Text(
                text = response.buttonTitle ?: ("Update New Version"),
                style = Typography.titleMedium
            )
        }


    }


}