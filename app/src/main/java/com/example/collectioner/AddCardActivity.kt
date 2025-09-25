package com.example.collectioner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.collectioner.ui.theme.CollectionerTheme


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.collectioner.ui.theme.PrimaryTextColor
import com.google.gson.Gson

class AddCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                val photoUri = intent.getStringExtra("photoUri") ?: ""
                AddCardScreen(photoUri = photoUri)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCardScreen(photoUri: String) {
    var nomeCarta by remember { mutableStateOf("") }
    var giocoDiCarte by remember { mutableStateOf("") }
    var numeroCarta by remember { mutableStateOf("") }
    var setCarta by remember { mutableStateOf("") }
    var raritaCarta by remember { mutableStateOf("") }
    var artistaCarta by remember { mutableStateOf("") }
    var condizioniCarta by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val context = androidx.compose.ui.platform.LocalContext.current

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
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )
            OutlinedTextField(
                value = giocoDiCarte,
                onValueChange = { giocoDiCarte = it },
                label = { Text("Gioco di carte", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = setCarta,
                    onValueChange = { setCarta = it },
                    label = { Text("Set", color = PrimaryTextColor) },
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )
                OutlinedTextField(
                    value = numeroCarta,
                    onValueChange = { numeroCarta = it },
                    label = { Text("Numero", color = PrimaryTextColor) },
                    modifier = Modifier.weight(1f),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
                )
            }
            OutlinedTextField(
                value = raritaCarta,
                onValueChange = { raritaCarta = it },
                label = { Text("Rarità", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )
            OutlinedTextField(
                value = artistaCarta,
                onValueChange = { artistaCarta = it },
                label = { Text("Artista", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )
            OutlinedTextField(
                value = condizioniCarta,
                onValueChange = { condizioniCarta = it },
                label = { Text("Condizioni carta", color = PrimaryTextColor) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    val cardData = CardData(
                        photoUri = photoUri,
                        nomeCarta = nomeCarta,
                        giocoDiCarte = giocoDiCarte.lowercase(),
                        numeroCarta = numeroCarta,
                        setCarta = setCarta,
                        raritaCarta = raritaCarta,
                        artistaCarta = artistaCarta,
                        condizioniCarta = condizioniCarta,
                        preferito = false // aggiunto campo preferito
                    )
                    val gson = Gson()
                    val fileName = "cards.json"
                    val file = java.io.File(context.filesDir, fileName)
                    val cardList: MutableList<CardData> = if (file.exists()) {
                        try {
                            val json = file.readText()
                            gson.fromJson(json, Array<CardData>::class.java)?.toMutableList() ?: mutableListOf()
                        } catch (e: Exception) {
                            mutableListOf()
                        }
                    } else {
                        mutableListOf()
                    }
                    cardList.add(cardData)
                    val jsonString = gson.toJson(cardList)
                    file.writeText(jsonString)
                    android.widget.Toast.makeText(context, "Carta salvata!", android.widget.Toast.LENGTH_SHORT).show()
                    val intent = Intent(context, ArchiveActivity::class.java)
                    context.startActivity(intent)
                },
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
    AddCardScreen(photoUri = "")
}


/*
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CollectionerTheme {
        Greeting("Android")
    }
}*/

