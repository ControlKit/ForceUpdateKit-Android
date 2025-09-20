package com.forceupdatekit.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.example.ui.theme.ForceUpdateKitTheme
import com.forceupdatekit.forceUpdateKitHost
import com.forceupdatekit.view.config.ForceUpdateViewConfig
import com.forceupdatekit.view.config.ForceUpdateViewStyle
import com.forceupdatekit.view.viewmodel.state.ForceUpdateState


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ForceUpdateKitTheme {

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp), color = Color.White
                ) {
                   val kit= forceUpdateKitHost(
                        ForceUpdateServiceConfig(
                            version = "1.0.0",
                            appId = "9fec5078-656e-489d-97fd-56c0f628394a",
                            deviceId = "dsd1",
                            timeRetryThreadSleep = 20000L,
                            viewConfig = ForceUpdateViewConfig(
                                ForceUpdateViewStyle.Popover2,
                            )
                            /*      imageView = {
                                Image(
                                    painter = painterResource(
                                        id = com.forceupdatekit.R.drawable.spaceship),
                                    contentDescription = null,
                                    )
                            }*/

                            /*         buttonView = { onClick ->

                                             Button(
                                                 onClick = {
                                                     onClick.invoke()
                                                 }
                                             ) {
                                                 Row {
                                                     Text(" دکمه")
                                                 }
                                             }
                                     }*/
                        ),
                        onDismiss = {

                        },
                        onState = { state ->
                            when (state) {
                                is ForceUpdateState.ShowViewError -> {
                                }
                                is ForceUpdateState.ShowView -> {
                                    Log.i("LOG", "onState:ShowView ${state.data}")
                                }

                                else -> {

                                }
                            }


                        }
                    )
                    kit.showView()
                }

            }
        }
    }
}





