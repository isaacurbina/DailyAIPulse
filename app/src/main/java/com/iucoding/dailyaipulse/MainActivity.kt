package com.iucoding.dailyaipulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.iucoding.dailyaipulse.articles.ui.ArticleScreen
import com.iucoding.dailyaipulse.ui.theme.DailyAIPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DailyAIPulseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    ArticleScreen()
                }
            }
        }
    }
}
