package com.koreatech.kotrip_android.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.koreatech.kotrip_android.presentation.theme.MotivooTheme
import com.koreatech.kotrip_android.presentation.views.MainNavHost

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotivooTheme {
                val navController = rememberNavController()
                MainNavHost(
                    navController = navController,
                )
            }
        }
    }
}