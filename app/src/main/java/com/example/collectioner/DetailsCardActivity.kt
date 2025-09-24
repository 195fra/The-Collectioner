package com.example.collectioner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.collectioner.ui.theme.CollectionerTheme
import com.google.gson.Gson

class DetailsCardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cardDataJson = intent.getStringExtra("cardDataJson")
        setContent {
            CollectionerTheme {
                DetailsCardScreen(cardDataJson = cardDataJson)
            }
        }
    }
}

@Composable
fun DetailsCardScreen(cardDataJson: String?) {
    val gson = Gson()
    val cardData = remember(cardDataJson) {
        if (cardDataJson != null) gson.fromJson(cardDataJson, CardData::class.java) else null
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (cardData != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val context = LocalContext.current
                    Button(
                        onClick = {
                            if (context is ComponentActivity) {
                                context.onBackPressedDispatcher.onBackPressed()
                            }
                        },

                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Torna indietro",
                            tint = Color.Black
                        )
                    }
                }
                Image(
                    painter = rememberAsyncImagePainter(cardData.photoUri),
                    contentDescription = "Immagine carta",
                    modifier = Modifier
                        .size(400.dp)
                        .padding(8.dp)
                )
                Text("Nome: ${cardData.nomeCarta}", style = MaterialTheme.typography.titleMedium)
                Text("Gioco: ${cardData.giocoDiCarte}")
                Text("Set: ${cardData.setCarta}")
                Text("Numero: ${cardData.numeroCarta}")
                Text("Rarità: ${cardData.raritaCarta}")
                Text("Artista: ${cardData.artistaCarta}")
                Text("Condizioni: ${cardData.condizioniCarta}")
            }
        } else {
            Text("Carta non trovata", color = Color.Red)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsCardScreenPreview() {
    DetailsCardScreen(cardDataJson = null)
}