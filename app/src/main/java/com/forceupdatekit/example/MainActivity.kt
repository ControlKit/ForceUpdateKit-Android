package com.forceupdatekit.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.forceupdatekit.ForceUpdateKit
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.example.ui.theme.ForceUpdateKitTheme
import com.forceupdatekit.view.config.ForceUpdateViewConfig
import com.forceupdatekit.view.config.ForceUpdateViewStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForceUpdateKitTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
//                    Example2()

                    WaveClipView()
                }

            }
        }
    }
}

@Composable
fun Example1() {
    ForceUpdateKit().Configure()
}

@Composable
fun Example2() {
    ForceUpdateKit(
        ForceUpdateServiceConfig(
//            version = "0.0.0",
//            route = "https://google.com/",
            viewConfig = ForceUpdateViewConfig(
                ForceUpdateViewStyle.Popover1
            )
        )
    ).Configure()
}

@Composable
fun Example3() {

    val viewConfig = ForceUpdateViewConfig()
    viewConfig.headerTitleColor = Color.Blue
    viewConfig.updateButtonColor = Color.Green
    viewConfig.updateButtonLayoutModifier = Modifier.padding(top = 80.dp)
    ForceUpdateKit(
        ForceUpdateServiceConfig(
            viewConfig = viewConfig
        )
    ).Configure()
}

@Composable
fun Example4() {

    val viewConfig = ForceUpdateViewConfig()
    viewConfig.buttonView = {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.Blue, modifier = Modifier
                .size(height = 48.dp, width = 200.dp)
        ) {
            Box {
                Text(
                    text = "UpdateButton",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )

            }
        }
    }
    ForceUpdateKit(
        ForceUpdateServiceConfig(
            viewConfig = viewConfig
        )
    ).Configure()
}

@Composable
fun WaveClipView() {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            Modifier.padding(top = 100.dp)
                .fillMaxWidth()
                .background(Color.Red)
                .fillMaxHeight(0.7f)

        ) {
            Surface(
                modifier = Modifier
                    .height(80.dp)
                    .width(85.dp) .offset(y = (-20).dp),
                shape = RoundedCornerShape(55.dp),
                color = Color.Red,

                ) {

            }
        }
    }

}
