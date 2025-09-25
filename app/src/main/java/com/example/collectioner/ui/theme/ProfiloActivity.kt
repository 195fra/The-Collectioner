package com.example.collectioner.ui.theme

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.collectioner.ui.theme.ui.theme.CollectionerTheme

class ProfiloActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollectionerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ProfiloScreen()
                }
            }
        }
    }
}

@Composable
fun ProfiloScreen() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val email = sharedPref.getString("email", "") ?: ""
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Profilo", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Email salvata:", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = email, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(32.dp))
            androidx.compose.material3.Button(onClick = {
                // Cancella la mail salvata
                sharedPref.edit().remove("email").apply()
                // Torna alla schermata MainActivity
                val intent = android.content.Intent(context, com.example.collectioner.MainActivity::class.java)
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                if (context is android.app.Activity) {
                    context.finish()
                }
            }) {
                Text("LOG OUT")
            }
        }
    }
}
