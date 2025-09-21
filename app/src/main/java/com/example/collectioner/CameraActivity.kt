package com.example.collectioner

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import com.example.collectioner.ui.theme.CollectionerTheme
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

sealed class Screen { object Camera : Screen(); object Gallery : Screen() }

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Camera) }
                var lastPhotoUri by remember { mutableStateOf<Uri?>(null) }
                when (currentScreen) {
                    is Screen.Camera -> CameraScreen(
                        onShowGallery = { currentScreen = Screen.Gallery },
                        onPhotoTaken = { lastPhotoUri = it }
                    )
                    is Screen.Gallery -> GalleryScreen(
                        onBack = { currentScreen = Screen.Camera },
                        lastPhotoUri = lastPhotoUri
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(onShowGallery: () -> Unit, onPhotoTaken: (Uri?) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )
    LaunchedEffect(true) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TopAppBar(title = { Text("Fotocamera") }, actions = {
            Button(onClick = onShowGallery) { Text("Galleria") }
        })
        if (hasCameraPermission) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val capture = ImageCapture.Builder().build()
                        imageCapture = capture
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, preview, capture
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val photoFile = File(
                    context.filesDir,
                    "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
                )
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            imageUri = FileProvider.getUriForFile(
                                context,
                                context.packageName + ".provider",
                                photoFile
                            )
                            Toast.makeText(context, "Foto salvata!", Toast.LENGTH_SHORT).show()
                            onPhotoTaken(imageUri)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(context, "Errore: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }) {
                Icon(Icons.Default.Face, contentDescription = "Scatta foto")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Scatta foto")
            }
            Spacer(modifier = Modifier.height(16.dp))
            imageUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = "Ultima foto",
                    modifier = Modifier.size(200.dp)
                )
            }
        } else {
            Text("Permesso fotocamera necessario")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(onBack: () -> Unit, lastPhotoUri: Uri?) {
    val context = LocalContext.current
    val images = remember {
        context.filesDir.listFiles { file -> file.name.endsWith(".jpg") || file.name.endsWith(".jpeg") }
            ?.sortedByDescending { it.lastModified() }
            ?.map { FileProvider.getUriForFile(context, context.packageName + ".provider", it) }
            ?: emptyList()
    }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("Galleria") }, navigationIcon = {
            Button(onClick = onBack) { Text("Indietro") }
        })
        if (images.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nessuna foto trovata")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(120.dp),
                modifier = Modifier.fillMaxSize().padding(8.dp)
            ) {
                items(images) { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(120.dp)
                            .clickable { /* Qui puoi aggiungere azioni, es: ingrandire la foto */ }
                    )
                }
            }
        }
    }
}

