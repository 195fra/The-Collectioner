package com.example.collectioner

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
import com.example.collectioner.ui.theme.CollectionerTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import com.example.collectioner.ui.theme.PrimaryTextColor

class ProfiloActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollectionerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize(), ) {
                        ProfiloScreen()
                        androidx.compose.material3.IconButton(
                            onClick = {
                                finish()
                            },
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(16.dp)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Torna alla Home"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfiloScreen() {
    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)
    val name = sharedPref.getString("name", "") ?: ""
    val surname = sharedPref.getString("surname", "") ?: ""
    val email = sharedPref.getString("email", "") ?: ""
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Profilo", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "$name", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$surname", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: $email", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(32.dp))
            androidx.compose.material3.Button(onClick = {
                sharedPref.edit().remove("email").remove("name").remove("surname").apply()
                val intent = android.content.Intent(context, MainActivity::class.java)
                intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP or android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
                if (context is android.app.Activity) {
                    context.finish()
                }
            },
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = PrimaryTextColor)
            ) {
                Text("LOG OUT", color = androidx.compose.ui.graphics.Color.White)
            }
        }
    }
}
