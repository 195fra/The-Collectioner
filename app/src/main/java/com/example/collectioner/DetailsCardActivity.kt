package com.example.collectioner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Imposta sfondo nero
        contentAlignment = Alignment.Center
    ) {
        if (cardData != null) {
            // Aggiungi lo scroll
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .widthIn(max = 400.dp)
                    .padding(16.dp)
                    .verticalScroll(scrollState), // Scroll verticale
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
                // Contenitore per Nome
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Nome:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.nomeCarta,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // Contenitore per Gioco
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Gioco:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.giocoDiCarte,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // Contenitore per Set e Numero sulla stessa riga
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Set:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.setCarta,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            "Numero:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.numeroCarta,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // Contenitore per Rarità
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Rarità:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.raritaCarta,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // Contenitore per Artista
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Artista:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.artistaCarta,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
                // Contenitore per Condizioni
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Condizioni:",
                            color = Color.White,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            cardData.condizioniCarta,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            Text("Carta non trovata", color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsCardScreenPreview() {
    DetailsCardScreen(cardDataJson = null)
}