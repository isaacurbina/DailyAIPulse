package com.iucoding.dailyaipulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iucoding.dailyaipulse.articles.ui.ArticleScreen
import com.iucoding.dailyaipulse.auth.LoginScreen
import com.iucoding.dailyaipulse.auth.SignupScreen
import com.iucoding.dailyaipulse.sources.ui.SourceScreen
import com.iucoding.dailyaipulse.ui.theme.DailyAIPulseTheme
import dagger.hilt.android.AndroidEntryPoint

enum class BottomNavItem(val label: Int, val icon: ImageVector) {
	ARTICLES(R.string.articles_screen_title, Icons.Default.Home),
	SOURCES(R.string.sources_screen_title, Icons.Default.Info)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		WindowCompat.setDecorFitsSystemWindows(window, false)

		setContent {
			DailyAIPulseTheme {
				Surface(
					modifier = Modifier
						.fillMaxSize()
						.systemBarsPadding()
				) {
					val navController = rememberNavController()

					NavHost(navController = navController, startDestination = "login") {
						composable("login") {
							LoginScreen(
								onLoginClick = {
									navController.navigate("main") {
										popUpTo("login") { inclusive = true }
									}
								},
								onCreateAccountClick = {
									navController.navigate("signup")
								}
							)
						}
						composable("signup") {
							SignupScreen(
								onSignupClick = {
									navController.navigate("main") {
										popUpTo("login") { inclusive = true }
									}
								},
								onLoginClick = {
									navController.navigate("login") {
										popUpTo("login") { inclusive = true }
									}
								},
								onBackClick = {
									navController.popBackStack()
								}
							)
						}
						composable("main") {
							MainContent()
						}
					}
				}
			}
		}
	}
}

@Composable
fun MainContent() {
	val selectedTab = remember { mutableStateOf(BottomNavItem.ARTICLES) }

	Scaffold(
		modifier = Modifier.fillMaxSize(),
		bottomBar = {
			NavigationBar {
				BottomNavItem.entries.forEach { item ->
					NavigationBarItem(
						icon = { Icon(imageVector = item.icon, contentDescription = stringResource(item.label)) },
						label = { Text(stringResource(item.label)) },
						selected = selectedTab.value == item,
						onClick = { selectedTab.value = item }
					)
				}
			}
		}
	) { innerPadding ->
		Surface(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			when (selectedTab.value) {
				BottomNavItem.ARTICLES -> ArticleScreen()
				BottomNavItem.SOURCES -> SourceScreen()
			}
		}
	}
}
