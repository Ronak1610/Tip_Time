/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tiptime

import android.os.Bundle
import android.widget.Switch
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.annotation.StringRes

import androidx.compose.material3.Icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiptime.ui.theme.TipTimeTheme
import androidx.compose.material3.Switch
import androidx.compose.foundation.layout.wrapContentWidth

import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            TipTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),

                ) {
                    TipTimeLayout()
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {

    var amountInput by remember {
        mutableStateOf("")
    }
    var tipInput by remember {
        mutableStateOf("")
    }
    var RoundUp by
    remember {
        mutableStateOf(false)
    }
    val amount=  amountInput.toDoubleOrNull() ?:0.0
    val tipPercent = tipInput.toDoubleOrNull() ?:0.0
    val tip = calculateTip(amount,tipPercent, RoundUp )

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 40.dp)
            .verticalScroll(rememberScrollState())

            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.calculate_tip),
            modifier = Modifier
                .padding(bottom = 16.dp, top = 40.dp)
                .align(alignment = Alignment.Start)
        )
       EdiTextField(label = R.string.bill_amount,
           leadingIcon = R.drawable.money,
           value = amountInput,
            onValueChange = {amountInput=it},

            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),)

        EdiTextField(label = R.string.how_was_the_service,
            leadingIcon = R.drawable.percent,
            value = tipInput,
            onValueChange = {tipInput=it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),)

        RoundTipRow(Checked = RoundUp, onROundUpChanged = {RoundUp = it}, modifier = Modifier.padding(bottom = 32.dp))
        
        Text(
            text = stringResource(R.string.tip_amount, tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }

}

@Composable
fun EdiTextField( @StringRes label :Int,
                  @DrawableRes leadingIcon :Int,
                   value: String,
                 onValueChange : (String)-> Unit,
                 modifier:Modifier=Modifier )
{

    TextField(value = value ,
        onValueChange = onValueChange,
        label = { Text(stringResource(id =label ))},
        modifier=modifier ,
        leadingIcon= { Icon(painter = painterResource(id = leadingIcon), contentDescription = null)},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next))

}

@Composable
fun RoundTipRow(Checked:Boolean,onROundUpChanged :(Boolean)-> Unit,modifier: Modifier=Modifier)
{

    Row(modifier = modifier
        .fillMaxWidth()
        .size(48.dp),
        verticalAlignment = Alignment.CenterVertically) {

        Text(text = stringResource(id = R.string.round_up_tip))

        Switch(modifier= Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
            ,checked = Checked, onCheckedChange = onROundUpChanged )

    }
}
private fun calculateTip(amount: Double, tipPercent: Double = 15.0, RoundUp : Boolean): String {
    var tip = tipPercent / 100 * amount
    if (RoundUp)
    {
        tip= kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)

}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview()
{

    TipTimeTheme {

        TipTimeLayout()

    }
}
