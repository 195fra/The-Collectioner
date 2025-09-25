package com.example.collectioner.ui.theme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collectioner.ArchiveActivity
import com.example.collectioner.CameraActivity
import com.example.collectioner.CardData
import com.example.collectioner.DetailsCardActivity
import com.example.collectioner.R
import com.example.collectioner.ui.theme.ui.theme.CollectionerTheme
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

                    //  Applica il padding fornito dallo Scaffold
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        /*// LazyRow
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            items(10) { index ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(120.dp)
                                        .height(80.dp)
                                        .background(
                                            Color.LightGray,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Favourite ${index + 1}", color = Color.Black)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        //Text(text = "Descrizione breve", color = Color.DarkGray)
                                    }
                                }
                            }
                        }*/


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
                                // Conta solo le carte delle 4 categorie specificate
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
                                    // = Color.White,
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
                                    //Text(text = preferita!!.giocoDiCarte, modifier = Modifier.padding(4.dp), color = Color.DarkGray)
                                    //Text(text = "Set: ${preferita!!.setCarta}", modifier = Modifier.padding(4.dp), color = Color.DarkGray)
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
                                    //Text(text = "Favourite Card", modifier = Modifier.padding(16.dp), color = Color.Black)
                                    Text(text = "Aggiungi una carta ai preferiti per poterla visualizzare qui", color = Color.White,
                                        textAlign = TextAlign.Center)
                                }
                            }

                        }

                        /*Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .background(Color(0xFFB3E5FC), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                    .clickable { /* azione box 1 */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Box cliccabile 1", color = Color.Black)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .background(Color(0xFFFFF9C4), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                    .clickable { /* azione box 2 */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Box cliccabile 2", color = Color.Black)
                            }
                        }*/
                        // Box con conteggio categorie in fondo
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

//@Preview(showBackground = true)
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
