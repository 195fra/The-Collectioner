package com.example.collectioner

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.core.content.FileProvider
import com.example.collectioner.ui.theme.CollectionerTheme
import com.example.collectioner.ui.theme.BottomTabBar

class ArchiveActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                ArchiveScreen()
            }
        }
    }
}

@Composable
fun ArchiveScreen() {
    var ricerca by remember { mutableStateOf("") }

    val categorie = listOf("tutti","pokemon", "yugioh", "magic", "dragonball")
    val imageResIds = listOf(
        R.drawable.image_0,
        R.drawable.image_1,
        R.drawable.image_2,
        R.drawable.image_3,
        R.drawable.image_4,
    )

    // Stato per tracciare la categoria selezionata
    var categoriaSelezionata by remember { mutableStateOf<String?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomTabBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = ricerca,
                onValueChange = { ricerca = it },
                label = { Text("Cerca") },
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
            )

            Text("My Albums")

            // Lista orizzontale con categorie
            Box(Modifier.height(100.dp)) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(categorie.size) { index ->
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    // Toggle selezione categoria
                                    categoriaSelezionata = if (index == 0) null
                                    else categorie[index]
                                },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = imageResIds[index]),
                                contentDescription = "Immagine categoria",
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(100.dp)
                            )
                            Text(
                                text = categorie[index],
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Foto salvate")

            // Griglia con immagini filtrate per categoria
            ImageGridFromFiles(categoriaFiltro = categoriaSelezionata)
        }
    }
}

@Composable
fun ImageGridFromFiles(categoriaFiltro: String?) {
    val context = LocalContext.current

    // Carica e filtra le immagini in base al nome file e alla categoria
    val images = remember(categoriaFiltro) {
        context.filesDir
            .listFiles { file ->
                val isImage = file.name.endsWith(".jpg", ignoreCase = true) || file.name.endsWith(".jpeg", ignoreCase = true)
                val matchesCategoria = categoriaFiltro == null || file.name.contains(categoriaFiltro, ignoreCase = true)
                isImage && matchesCategoria
            }
            ?.sortedByDescending { it.lastModified() }
            ?.map {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    it
                )
            } ?: emptyList()
    }

    if (images.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Nessuna immagine trovata.")
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(images) { uri: Uri ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f),
                        //.clickable { /* Azione click su immagine */ },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Foto salvata",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}
