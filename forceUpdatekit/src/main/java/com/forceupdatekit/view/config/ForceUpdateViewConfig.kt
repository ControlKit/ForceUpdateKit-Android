package com.forceupdatekit.view.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.forceupdatekit.theme.Black100

data class ForceUpdateViewConfig(
    var forceUpdateViewStyle:   ForceUpdateViewStyle = ForceUpdateViewStyle.Popover1,
    var placeholderImageDrawble:   Int? = null,
    var imageDrawble:   Int? = null,
    var errorImageDrawble:   Int? = null,
    var contentScaleImageDrawble:   ContentScale? = null,
    var updateImageColor:   Color? = null,
    var imageLayoutModifier:   Modifier? = null,
    var popupViewLayoutModifier: Modifier? = null,
    var popupViewBackGroundColor: Color? = null,
    var popupViewCornerRadius: Dp? = null,
    var headerTitle: String = "It's time to update",
    var headerTitleColor: Color? = null,
    var headerTitleLayoutModifier:   Modifier? = null,
    var descriptionTitle: String = "The version you are using is old, need to update the latest version in order to experience new features.",
    var descriptionTitleColor: Color? = null,
    var descriptionTitleLayoutModifier:   Modifier? = null,
    var lineTitleColor: Color? = null,
    var lineLayoutModifier:   Modifier? = null,
    var buttonTitle: String = "Update New Version",
    var buttonTitleColor: Color? = null,
    var buttonColor: Color? = null,
    var buttonCornerRadius: Dp? = null,
    var buttonBorderColor: Color? = null,
    var buttonLayoutModifier:   Modifier? = null,
    var versionTitle: String = "Up to 12.349 version Apr 2024.",
    var versionTitleColor: Color? = null,
    var versionTitleLayoutModifier:   Modifier? = null,

    var imageView: @Composable ((String) -> Unit)? = null,
    var versionTitleView: @Composable ((String) -> Unit)? = null,
    var headerTitleView: @Composable ((String) -> Unit)? = null,
    var descriptionTitleView: @Composable ((String) -> Unit)? = null,

    var lineView: @Composable (() -> Unit)? = null,
    var buttonView: (@Composable (libraryOnClick: () -> Unit) -> Unit)? = null,
    var noUpdateState: (() -> Unit)? = null,

    var retryViewTitleLayoutModifier:   Modifier? = null,
    var retryViewTitleView: @Composable (() -> Unit)? = null,
    var retryViewTitle: String = "Connection Lost",
    var retryViewTitleColor: Color? = null,
    var retryViewTryAgainButtonLayoutModifier:   Modifier? = null,
    var retryViewTryAgainButtonView: @Composable (() -> Unit)? = null,
    var retryViewTryAgainButtonCornerRadius: Dp? = null,
    var retryViewTryAgainButtonColor: Color? = null,
    var retryViewTryAgainButtonTitle: String = "Try Again",
    var retryViewCancelButtonLayoutModifier:   Modifier? = null,
    var retryViewCancelButtonView: @Composable (() -> Unit)? = null,
    var retryViewCancelButtonCornerRadius: Dp? = null,
    var retryViewCancelButtonColor: Color? = null,
    var retryViewCancelButtonTitle: String = "Cancel",
    var retryViewImageLayoutModifier:   Modifier? = null,
    var retryViewImageView: @Composable (() -> Unit)? = null,
    var retryViewUpdateImageDrawble:   Int? = null,
    var retryViewImageColor:   Color = Black100,
    var retryViewSpace:   @Composable (() -> Unit)? = null,
    var retryViewCancelButton: (() -> Unit)? = null,


    )
