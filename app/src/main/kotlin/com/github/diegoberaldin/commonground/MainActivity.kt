package com.github.diegoberaldin.commonground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.diegoberaldin.commonground.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.appearance.theme.CommonGroundTheme
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.feature.imagedetail.ImageDetailScreen
import com.github.diegoberaldin.commonground.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val uiTheme = if (isSystemInDarkTheme()) {
                UiTheme.Dark
            } else {
                UiTheme.Light
            }
            val themeRepository = rememberByInjection<ThemeRepository>()
            LaunchedEffect(themeRepository) {
                themeRepository.changeTheme(uiTheme)
            }
            CommonGroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavigationDestination.Home.route,
                    ) {
                        buildNavGraph(
                            navController = navController,
                        )
                    }
                }
            }
        }
    }
}

private fun NavGraphBuilder.buildNavGraph(
    navController: NavHostController,
) {
    composable(NavigationDestination.Home.route) {
        HomeScreen(
            onOpenDetail = { imageId ->
                navController.navigate(
                    route = NavigationDestination.ImageDetail.withActualParams(
                        params = mapOf("id" to imageId)
                    )
                )
            }
        )
    }
    composable(
        route = NavigationDestination.ImageDetail.route,
        arguments = listOf(
            navArgument("id") {
                type = NavType.StringType
                nullable = false
            },
        )
    ) {
        val imageId = it.arguments?.getString("id").orEmpty()
        ImageDetailScreen(id = imageId)
    }
}
