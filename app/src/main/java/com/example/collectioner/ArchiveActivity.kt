package com.example.collectioner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collectioner.ui.theme.CollectionerTheme
import com.example.collectioner.ui.theme.BottomTabBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person

class ArchiveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                var ricerca by remember {
                    mutableStateOf("")
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),

                    bottomBar = {
                        BottomTabBar()
                    }
                ) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp)
                    ){
                        OutlinedTextField(
                            value = ricerca,
                            onValueChange = { ricerca = it },
                            label = { Text("Cerca") },
                            modifier = Modifier
                                .padding(20.dp)
                                .fillMaxWidth()
                        )

                        Text("My Albums")

                        // Lista di elementi cliccabili con immagine e testo
                        Box(Modifier.height(200.dp)){
                            androidx.compose.foundation.lazy.LazyRow(
                                modifier = Modifier.fillMaxSize().padding(8.dp)
                            ) {
                                items(10) { index ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight()
                                            .padding(vertical = 8.dp)
                                            .clickable { /* Azione click */ },
                                    ) {
                                        androidx.compose.foundation.Image(
                                            painter = androidx.compose.ui.res.painterResource(id = com.example.collectioner.R.drawable.ic_launcher_foreground),
                                            contentDescription = "Immagine elemento",
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                        Text(text = "Elemento $index", modifier = Modifier.padding(horizontal = 16.dp))
                                    }
                                }
                            }
                        }


                        Text("NOME_ALBUM Collection")

                        // GridView scrollabile con 3 elementi per riga
                        androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                            columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .height(300.dp)
                                .padding(top = 8.dp),
                            content = {
                                items(30) { index ->
                                    Box(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .fillMaxWidth()
                                            .aspectRatio(1f)
                                            .clickable { /* Azione click elemento grid */ },
                                        contentAlignment = androidx.compose.ui.Alignment.Center
                                    ) {
                                        androidx.compose.foundation.Image(
                                            painter = androidx.compose.ui.res.painterResource(id = com.example.collectioner.R.drawable.ic_launcher_foreground),
                                            contentDescription = "Immagine elemento grid",
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
