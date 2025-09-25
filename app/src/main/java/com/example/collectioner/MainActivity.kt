package com.example.collectioner

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.collectioner.ui.theme.CollectionerTheme
// nuovi import
import androidx.navigation.NavController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.collectioner.ui.theme.HomeActivity
import com.example.collectioner.ui.theme.PrimaryTextColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.IconButton
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val savedEmail = sharedPref.getString("email", null)
        if (savedEmail != null && savedEmail.isNotEmpty()) {
            // Se l'email è già salvata, vai direttamente alla HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        setContent {
            CollectionerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "Login_Screen") {
                    composable("Login_Screen") { Login_Screen(navController) }
                    composable("Home_Screen") { Home_Screen() }
                }
            }
        }
    }
}

@Composable
fun Login_Screen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var surname by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf(false) }
    var surnameError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    var passwordVisible by remember { mutableStateOf(false) }



    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ){
        Spacer(modifier = Modifier.height(10.dp))


        Text("LOGIN",
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.CenterHorizontally)
                .padding(top = 30.dp),
            fontSize = 30.sp,
            color = PrimaryTextColor
        )


        Spacer(modifier = Modifier.height(10.dp))


        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
                nameError = it.isBlank()
            },
            label = { Text("Nome") },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            isError = nameError,
            supportingText = { if (nameError) Text("Il nome non può essere vuoto", color = Color.Red, fontSize = 12.sp) }
        )
        OutlinedTextField(
            value = surname,
            onValueChange = {
                surname = it
                surnameError = it.isBlank()
            },
            label = { Text("Cognome") },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            isError = surnameError,
            supportingText = { if (surnameError) Text("Il cognome non può essere vuoto", color = Color.Red, fontSize = 12.sp) }
        )
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()
            },
            label = { Text("Email") },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            isError = emailError,
            supportingText = { if (emailError) Text("Inserisci una mail valida", color = Color.Red, fontSize = 12.sp) }
        )
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError = !isPasswordValid(it)
            },
            label = { Text("Password") },
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            textStyle = androidx.compose.ui.text.TextStyle(color = Color.Black),
            isError = passwordError,
            supportingText = { if (passwordError) Text("La password deve contenere almeno 5 caratteri, un numero e un carattere speciale", color = Color.Red, fontSize = 12.sp) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility
                val description = if (passwordVisible) "Nascondi password" else "Mostra password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    androidx.compose.material3.Icon(imageVector = image, contentDescription = description)
                }
            }
        )


        Spacer(modifier = Modifier.weight(1f))


        Button(onClick = {
            nameError = name.isBlank()
            surnameError = surname.isBlank()
            emailError = !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            passwordError = !isPasswordValid(password)
            if (!nameError && !surnameError && !emailError && !passwordError) {
                // Salva la mail, nome e cognome se compilati
                if (email.isNotBlank()) {
                    activity?.getSharedPreferences("user_prefs", android.content.Context.MODE_PRIVATE)?.edit()?.apply {
                        putString("email", email)
                        putString("name", name)
                        putString("surname", surname)
                        apply()
                    }
                }
                val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent)
            }
        },
        colors = ButtonColors(
            containerColor = PrimaryTextColor,
            contentColor = Color.White,
            disabledContentColor = PrimaryTextColor,
            disabledContainerColor = Color.White
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(horizontal = 20.dp, vertical = 20.dp),
        ) {
            Text("Accedi")
        }
        Spacer(modifier = Modifier.height(70.dp))

    }
}

@Composable
fun Home_Screen (){
    Column{
        Text("Home screen")
    }
}

// Funzione di validazione password
fun isPasswordValid(password: String): Boolean {
    val specialChar = password.any { !it.isLetterOrDigit() }
    val digit = password.any { it.isDigit() }
    return password.length >= 5 && specialChar && digit
}
