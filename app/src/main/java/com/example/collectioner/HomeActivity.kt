package com.example.collectioner

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.collectioner.ui.theme.PrimaryBackgroundColor
import com.example.collectioner.ui.theme.PrimaryTextColor
import com.example.collectioner.ui.theme.CollectionerTheme
import com.google.gson.Gson



class HomeActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("The Collectioner", color = PrimaryTextColor) },
                            navigationIcon = {
                                IconButton(onClick = {
                                    val intent = Intent(this@HomeActivity, ProfiloActivity::class.java)
                                    startActivity(intent)
                                }) {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = "Profilo",
                                        tint = PrimaryBackgroundColor
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomTabBar(index = 0)
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {

                        // Box centrale: mostra la carta preferita
                        val context = LocalContext.current
                        val gson = Gson()
                        var preferita by remember { mutableStateOf<CardData?>(null) }
                        var cardCounts by remember { mutableStateOf(mapOf<String, Int>()) }
                        val categorie = listOf("pokemon", "yugioh", "magic", "dragonball")
                        LaunchedEffect(Unit) {
                            val file = java.io.File(context.filesDir, "cards.json")
                            if (file.exists()) {
                                val json = file.readText()
                                val cardList = gson.fromJson(json, Array<CardData>::class.java)?.toList() ?: emptyList()
                                preferita = cardList.firstOrNull { it.preferito }

                                val counts = categorie.associateWith { cat ->
                                    cardList.count { it.giocoDiCarte == cat }
                                }
                                cardCounts = counts
                            } else {
                                cardCounts = categorie.associateWith { 0 }
                            }
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(500.dp)
                                .padding(16.dp)
                                .background(
                                    PrimaryBackgroundColor,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (preferita != null) {
                                Column(horizontalAlignment = CenterHorizontally) {
                                    Image(
                                        painter = coil.compose.rememberAsyncImagePainter(preferita!!.photoUri),
                                        contentDescription = "Immagine carta preferita",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(400.dp)
                                            .padding(16.dp)

                                    )
                                    Text(text = preferita!!.nomeCarta, modifier = Modifier.padding(4.dp), color = Color.White)
                                }
                            } else {
                                Column(horizontalAlignment = CenterHorizontally) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_camera),
                                        contentDescription = "Immagine di esempio",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .padding(16.dp),
                                        colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                                    )
                                    Text(text = "Aggiungi una carta ai preferiti per poterla visualizzare qui", color = Color.White,
                                        textAlign = TextAlign.Center)
                                }
                            }

                        }

                        // Box con conteggio categorie
                        Spacer(modifier = Modifier.height(24.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .background(
                                    PrimaryBackgroundColor,
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Text("LE MIE CARTE", color = Color.White, modifier = Modifier.align(Alignment.CenterHorizontally))
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    categorie.forEach { cat ->
                                        Column(horizontalAlignment = CenterHorizontally) {
                                            Text(text = cat, color = Color.White)
                                            Text(
                                                text = cardCounts[cat]?.toString() ?: "0",
                                                color = Color.White,
                                                style = MaterialTheme.typography.titleLarge
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun BottomTabBar(index : Int) {
    val context = LocalContext.current
    var selectedIndex by remember { mutableStateOf(index) }
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent); },
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { val intent = Intent(context, CameraActivity::class.java)
                context.startActivity(intent);},
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Scan"
                )
            },
            label = { Text("Scan") }
        )
        NavigationBarItem(
            selected = selectedIndex == 2,
            onClick = { val intent = Intent(context, ArchiveActivity::class.java)
                context.startActivity(intent);},
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_storage),
                    contentDescription = "Archivio"
                )
            },
            label = { Text("Archivio") }
        )
    }
}
