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
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.collectioner.ui.theme.BottomTabBar
import com.example.collectioner.ui.theme.CollectionerTheme
import com.google.gson.Gson
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

data class CardData(
    val photoUri: String,
    val nomeCarta: String,
    val giocoDiCarte: String,
    val numeroCarta: String,
    val setCarta: String,
    val raritaCarta: String,
    val artistaCarta: String,
    val condizioniCarta: String
)

class CameraActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                var lastPhotoUri by remember { mutableStateOf<Uri?>(null) }
                Scaffold (
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomTabBar() }
                ) { innerPadding ->
                    CameraScreen(
                        onPhotoTaken = { uri ->
                            lastPhotoUri = uri
                            // Passa l'URI a AddCardActivity
                            uri?.let {
                                val intent = android.content.Intent(this, AddCardActivity::class.java)
                                intent.putExtra("photoUri", it.toString())
                                startActivity(intent)
                            }
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    onPhotoTaken: (Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
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

    var photoName by remember { mutableStateOf("") }  // per rinominare la foto

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        //TopAppBar(title = { Text("Fotocamera") })

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
            Text("Categorizza la carta")
            androidx.compose.material3.OutlinedTextField(
                value = photoName,
                onValueChange = { photoName = it },
                placeholder = { Text("Es. pokemon") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                val validName = if (photoName.isNotBlank()) photoName else
                    "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
                // Se il nome esiste già, aggiungi un suffisso numerico per evitare sovrascrittura
                var fileNameWithExtension = if (validName.endsWith(".jpg")) validName else "$validName.jpg"
                val dir = context.filesDir
                var file = File(dir, fileNameWithExtension)
                var suffix = 1
                while (file.exists()) {
                    val baseName = validName.removeSuffix(".jpg")
                    fileNameWithExtension = "${baseName}_$suffix.jpg"
                    file = File(dir, fileNameWithExtension)
                    suffix++
                }
                val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            imageUri = FileProvider.getUriForFile(
                                context,
                                context.packageName + ".provider",
                                file
                            )
                            Toast.makeText(context, "Foto salvata come $fileNameWithExtension", Toast.LENGTH_SHORT).show()
                            onPhotoTaken(imageUri)
                        }

                        override fun onError(exception: ImageCaptureException) {
                            Toast.makeText(context, "Errore: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }) {
                Text("Scannerizza")
            }

            Spacer(modifier = Modifier.height(16.dp))
            /*if (imageUri != null) {
                Text("Foto scattata")
            }*/
        } else {
            Text("Permesso fotocamera necessario")
        }
    }
}
