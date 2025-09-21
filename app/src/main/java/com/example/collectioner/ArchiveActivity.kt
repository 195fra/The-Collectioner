package com.example.collectioner

import android.os.Bundle
import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import androidx.core.content.FileProvider
import com.example.collectioner.ui.theme.CollectionerTheme
import com.example.collectioner.ui.theme.BottomTabBar
import java.io.File

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

            // Lista orizzontale (placeholder)
            Box(Modifier.height(200.dp)) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    items(10) { index ->
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 8.dp)
                                .clickable { /* Azione click */ },
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(model = R.drawable.ic_launcher_foreground),
                                contentDescription = "Immagine elemento",
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .size(100.dp)
                            )
                            Text(
                                text = "Elemento $index",
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Foto salvate")

            ImageGridFromFiles()
        }
    }
}

@Composable
fun ImageGridFromFiles() {
    val context = LocalContext.current

    // Carica le immagini salvate nei file locali
    val images = remember {
        context.filesDir
            .listFiles { file -> file.name.endsWith(".jpg") || file.name.endsWith(".jpeg") }
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
                        .aspectRatio(1f)
                        .clickable { /* Azione click */ },
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
