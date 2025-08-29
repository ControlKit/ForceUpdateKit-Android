package com.forceupdatekit.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.forceupdatekit.ForceUpdateKit
import com.forceupdatekit.config.ForceUpdateServiceConfig
import com.forceupdatekit.example.ui.theme.ForceUpdateKitTheme
import com.forceupdatekit.theme.Black100
import com.forceupdatekit.theme.Black80
import com.forceupdatekit.view.config.ForceUpdateViewConfig
import com.forceupdatekit.view.config.ForceUpdateViewStyle
import kotlinx.coroutines.delay


lateinit var forceUpdateKit: ForceUpdateKit

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
                    Example()
                }

            }
        }
    }
}

@Composable
fun RadioButtonSingleSelection(
    modifier: Modifier = Modifier,
    platformId: MutableState<String>,
    selectedOption: MutableState<ForceUpdateViewStyle>,
) {

    Column(modifier.selectableGroup()) {
        ForceUpdateViewStyle.entries.forEach { style ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .selectable(
                        selected = (style == selectedOption.value),
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                        onClick = { selectedOption.value = style },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (style == selectedOption.value), onClick = null
                )
                Text(
                    text = style.name,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(50.dp))

        TextField(platformId)

    }
}

@Composable
fun TextField(text: MutableState<String>) {


    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("platformId", color = Black100) },
        placeholder = { Text("Enter your platformId") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "User icon",
                tint = Black80
            )
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp), // گوشه‌های گردتر
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = Black100,
            unfocusedTextColor = Black100,
            focusedPlaceholderColor = Black80
        )
    )
}

@Composable
fun Example() {

    var showForceUpdate by remember { mutableStateOf(false) }

    val selectedOption = remember { mutableStateOf(ForceUpdateViewStyle.FullScreen1) }
    val text = remember { mutableStateOf("9fb29c9d-0c93-49c9-9168-610e1ce8dac3") }



    forceUpdateKit = ForceUpdateKit(
        ForceUpdateServiceConfig(
            version = "0",
            appId = text.value,
            deviceId = "dsd",
            route = "https://tauri.ir/api/force-updates",
            viewConfig = ForceUpdateViewConfig(
                selectedOption.value,

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
            )
        )
    )


    Column(Modifier.fillMaxSize()) {
        RadioButtonSingleSelection(
            Modifier.weight(1f), text, selectedOption
        )
        Button(
            onClick = {
                showForceUpdate = true
            }, modifier = Modifier
                .padding(10.dp, bottom = 10.dp)
                .fillMaxWidth()
                .height(46.dp)
        ) {
            Text("show force update")
        }
    }
    if (showForceUpdate) {
        forceUpdateKit.Configure(onDismiss = {
            showForceUpdate = false

        })

    }
}





