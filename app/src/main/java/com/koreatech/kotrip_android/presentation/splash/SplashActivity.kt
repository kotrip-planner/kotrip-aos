package com.koreatech.kotrip_android.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.koreatech.kotrip_android.presentation.theme.MotivooTheme

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotivooTheme {
test
            }
        }
    }
}