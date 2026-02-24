// kotlin
package com.iucoding.dailyaipulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.iucoding.dailyaipulse.articles.ui.ArticleScreen
import com.iucoding.dailyaipulse.ui.theme.DailyAIPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		// Enable edge-to-edge handling and let Compose apply insets
		WindowCompat.setDecorFitsSystemWindows(window, false)

		setContent {
			DailyAIPulseTheme {
				Surface(
					modifier = Modifier
						.fillMaxSize()
						.systemBarsPadding()
				) {
					ArticleScreen()
				}
			}
		}
	}
}
