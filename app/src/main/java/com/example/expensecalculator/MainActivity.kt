package com.example.expensecalculator

import android.icu.text.NumberFormat
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.expensecalculator.ui.theme.ExpenseCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ExpenseCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                ExpenseCalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun ExpenseCalculatorLayout() {
    var balanceInput by remember { mutableStateOf("") }
    var groceryInput by remember { mutableStateOf("") }
    var electricityInput by remember { mutableStateOf("") }
    var waterInput by remember { mutableStateOf("") }
    var gasInput by remember { mutableStateOf("") }
    var fuelInput by remember { mutableStateOf("") }
    var internetInput by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val balance = balanceInput.toDoubleOrNull() ?: 0.0
    val grocery = groceryInput.toDoubleOrNull() ?: 0.0
    val electricity = electricityInput.toDoubleOrNull() ?: 0.0
    val water = waterInput.toDoubleOrNull() ?: 0.0
    val gas = gasInput.toDoubleOrNull() ?: 0.0
    val fuel = fuelInput.toDoubleOrNull() ?: 0.0
    val left_over = calculateLeftOver(balance, grocery, electricity, water, gas, fuel)

    if (showDialog) {
        MinimalDialog(onDismissRequest = { showDialog = false })
    }
    Column (
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.total_balance),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.balance,
            leadingIcon = R.drawable.money_bills_solid,
            value = balanceInput,
            onValueChanged = { balanceInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.expenses),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.grocery,
            leadingIcon = R.drawable.bag_shopping,
            value = groceryInput,
            onValueChanged = { groceryInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.electricity,
            leadingIcon = R.drawable.bolt_solid,
            value = electricityInput,
            onValueChanged = { electricityInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.water,
            leadingIcon = R.drawable.faucet_drip_solid,
            value = waterInput,
            onValueChanged = { waterInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.gas,
            leadingIcon = R.drawable.fire_flame_simple_solid,
            value = gasInput,
            onValueChanged = { gasInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.fuel,
            leadingIcon = R.drawable.gas_pump_solid,
            value = fuelInput,
            onValueChanged = { fuelInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.internet,
            leadingIcon = R.drawable.wifi_solid,
            value = internetInput,
            onValueChanged = { internetInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        FloatingButton(
            onClick = {showDialog = true}
        )



    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseCalculatorLayoutPreview() {
    ExpenseCalculatorTheme {
        ExpenseCalculatorLayout()
    }
}



@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    action: ImeAction = ImeAction.Next
){
    TextField(
        value = value,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        singleLine = true,
        modifier = modifier,
        onValueChange = onValueChanged,
        label = { Text(stringResource(label)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
            imeAction = action
        )
    )
}

@Composable
fun FloatingButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon = { Icon(painter = painterResource(id = R.drawable.calculator_solid), "Calculate") },
        text = { Text(text = "Calculate") },
    )
}

@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = "This is a minimal dialog",
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center),
                textAlign = TextAlign.Center,
            )
        }
    }
}






