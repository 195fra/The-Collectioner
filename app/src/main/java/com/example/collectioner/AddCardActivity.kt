package com.example.collectioner

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collectioner.PrimaryTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen() {
    var nomeCarta by remember { mutableStateOf("") }
    var giocoDiCarte by remember { mutableStateOf("") }
    var numeroCarta by remember { mutableStateOf("") }
    var setCarta by remember { mutableStateOf("") }
    var raritaCarta by remember { mutableStateOf("") }
    var artistaCarta by remember { mutableStateOf("") }
    var condizioniCarta by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Aggiungi una nuova carta",
                color = PrimaryTextColor,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = nomeCarta,
                onValueChange = { nomeCarta = it },
                label = { Text("Nome carta", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = giocoDiCarte,
                onValueChange = { giocoDiCarte = it },
                label = { Text("Gioco di carte", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = setCarta,
                    onValueChange = { setCarta = it },
                    label = { Text("Set", color = PrimaryTextColor) },
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = numeroCarta,
                    onValueChange = { numeroCarta = it },
                    label = { Text("Numero", color = PrimaryTextColor) },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = raritaCarta,
                onValueChange = { raritaCarta = it },
                label = { Text("Rarità", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = artistaCarta,
                onValueChange = { artistaCarta = it },
                label = { Text("Artista", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = condizioniCarta,
                onValueChange = { condizioniCarta = it },
                label = { Text("Condizioni carta", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTextColor)
            ) {
                Text("Aggiungi", color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddCardScreenPreview() {
    AddCardScreen()
}
