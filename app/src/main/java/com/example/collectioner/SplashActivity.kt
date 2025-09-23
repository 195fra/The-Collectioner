/* package com.example.collectioner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.collectioner.R
import kotlinx.coroutines.delay

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedSplashScreen()
        }
    }
}

@Composable
fun AnimatedSplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000) // Durata della splash screen
        navController.navigate("Login_Screen") {
            popUpTo("AnimatedSplashScreen") { inclusive = true }
        }
    }

    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    val offsetY = remember { Animatable(300f) } // parte dal basso (300dp)

    LaunchedEffect(Unit) {
        // Animazione in parallelo
        scale.animateTo(targetValue = 1f, animationSpec = tween(1200))
        alpha.animateTo(targetValue = 1f, animationSpec = tween(1200))
        offsetY.animateTo(targetValue = 0f, animationSpec = tween(1200))
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.the_collectioner_logo),
            contentDescription = "Logo",
            modifier = Modifier
                .size(150.dp)
                .scale(scale.value)
                .alpha(alpha.value)
                .offset(y = offsetY.value.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AnimatedSplashScreenPreview() {
    AnimatedSplashScreen()
}

*/