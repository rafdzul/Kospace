package com.example.kospace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kospace.ui.navigation.KospaceNavHost
import com.example.kospace.ui.theme.KospaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KospaceTheme {
                KospaceNavHost()
            }
        }
    }
}
