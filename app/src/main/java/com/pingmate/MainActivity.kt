package com.pingmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.pingmate.navigation.NavigationGraph
import com.pingmate.ui.theme.PingMateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PingMateTheme {
                val navController = rememberNavController()
                NavigationGraph(navController)
            }
        }
    }
}
