package com.example.collectioner.ui.theme

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.collectioner.ArchiveActivity
import com.example.collectioner.R
import com.example.collectioner.ui.theme.ui.theme.CollectionerTheme



class HomeActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollectionerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text("The Collectioner") },
                            navigationIcon = {
                                IconButton(onClick = { /* azione profilo */ }) {
                                    Icon(Icons.Filled.Person, contentDescription = "Profilo")
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomTabBar()
                    }
                ) { innerPadding ->

                    //  Applica il padding fornito dallo Scaffold
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // LazyRow visibile correttamente
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            items(10) { index ->
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp)
                                        .width(120.dp)
                                        .height(80.dp)
                                        .background(
                                            Color.LightGray,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(text = "Favourite ${index + 1}", color = Color.Black)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        //Text(text = "Descrizione breve", color = Color.DarkGray)
                                    }
                                }
                            }
                        }


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(16.dp)
                                .background(
                                    Color(0xFFE0E0E0),
                                    shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column (horizontalAlignment = CenterHorizontally){

                                Image(
                                    painter = painterResource(id = R.drawable.ic_camera),
                                    contentDescription = "Immagine di esempio",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .padding(16.dp)
                                )
                                Text(text = "Favourite Card",
                                    modifier = Modifier
                                        .padding(16.dp),
                                    color = Color.Black,
                                    )
                            }

                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .background(Color(0xFFB3E5FC), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                    .clickable { /* azione box 1 */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Box cliccabile 1", color = Color.Black)
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(80.dp)
                                    .background(Color(0xFFFFF9C4), shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                                    .clickable { /* azione box 2 */ },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Box cliccabile 2", color = Color.Black)
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomTabBar() {

    val context = LocalContext.current

    var selectedIndex by remember { mutableStateOf(0) }
    NavigationBar {
        NavigationBarItem(
            selected = selectedIndex == 0,
            onClick = { val intent = Intent(context, HomeActivity::class.java)
                context.startActivity(intent); selectedIndex= 0},
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = selectedIndex == 1,
            onClick = { selectedIndex = 1 },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Scan"
                )
            },
            label = { Text("Scan") }
        )
        NavigationBarItem(

            selected = selectedIndex == 2,
            onClick = { val intent = Intent(context, ArchiveActivity::class.java)
                context.startActivity(intent);selectedIndex=2},
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_storage),
                    contentDescription = "Archivio"
                )
            },
            label = { Text("Archivio") }
        )
    }
}
