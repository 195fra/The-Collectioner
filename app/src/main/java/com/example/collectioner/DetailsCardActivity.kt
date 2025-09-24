package com.example.collectioner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val context = LocalContext.current
                    var preferito by remember { mutableStateOf(cardData.preferito) }


                    Button(
                        onClick = {
                            val intent = Intent(context, ArchiveActivity::class.java)
                            context.startActivity(intent)
                        },

                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Torna indietro",
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(onClick = {
                        preferito = !preferito
                        // Aggiorna il valore nel JSON
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
                        val index = cardList.indexOfFirst { it.nomeCarta == cardData.nomeCarta && it.numeroCarta == cardData.numeroCarta }
                        if (index != -1) {
                            cardList[index] = cardList[index].copy(preferito = preferito)
                            file.writeText(gson.toJson(cardList))
                        }
                    }) {
                        Icon(
                            imageVector = if (preferito) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = if (preferito) "Preferito" else "Non preferito",
                            tint = if (preferito) Color.Red else Color.Gray
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