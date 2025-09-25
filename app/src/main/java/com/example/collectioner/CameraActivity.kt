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
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.collectioner.ui.theme.CollectionerTheme
import com.example.collectioner.ui.theme.PrimaryBackgroundColor
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
    val condizioniCarta: String,
    val preferito: Boolean = false
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
                    bottomBar = { BottomTabBar(index = 1) }
                ) { innerPadding ->
                    CameraScreen(
                        onPhotoTaken = { uri ->
                            lastPhotoUri = uri
                            // Passa l'URI a AddCardActivity se non è nullo
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

    var photoName by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (hasCameraPermission) {
            AndroidView(
                //funzione per fare la foto
                factory = { context ->
                    val previewView = PreviewView(context)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
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
                    }, ContextCompat.getMainExecutor(context))
                    previewView
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { // salva l'immagine
                    val validName = if (photoName.isNotBlank()) photoName else
                        "IMG_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
                    var fileNameWithExtension = if (validName.endsWith(".jpg")) validName else "$validName.jpg"
                    val dir = context.filesDir
                    var file = File(dir, fileNameWithExtension)
                    // evita di avere nomi duplicati
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
                                // rende l'immagine accessibile
                                imageUri = FileProvider.getUriForFile(
                                    context,
                                    context.packageName + ".provider",
                                    file
                                )
                                Toast.makeText(context, "Foto salvata", Toast.LENGTH_SHORT).show()
                                onPhotoTaken(imageUri)
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Toast.makeText(context, "Errore: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = PrimaryBackgroundColor)
            ) {
                Text("Scannerizza")
            }

            Spacer(modifier = Modifier.height(16.dp))

        } else {
            Text("Permesso fotocamera necessario")
        }
    }
}
