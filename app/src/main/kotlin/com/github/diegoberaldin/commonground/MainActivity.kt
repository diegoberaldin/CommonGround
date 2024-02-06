package com.github.diegoberaldin.commonground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.github.diegoberaldin.commonground.core.appearance.repository.ThemeRepository
import com.github.diegoberaldin.commonground.core.appearance.repository.UiTheme
import com.github.diegoberaldin.commonground.core.appearance.theme.CommonGroundTheme
import com.github.diegoberaldin.commonground.core.appearance.theme.Spacing
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerCoordinator
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerEvent
import com.github.diegoberaldin.commonground.core.commonui.drawer.DrawerSection
import com.github.diegoberaldin.commonground.core.utils.rememberByInjection
import com.github.diegoberaldin.commonground.feature.drawer.DrawerContent
import com.github.diegoberaldin.commonground.feature.favorites.FavoritesScreen
import com.github.diegoberaldin.commonground.feature.imagedetail.ImageDetailScreen
import com.github.diegoberaldin.commonground.feature.imagelist.ImageListScreen
import com.github.diegoberaldin.commonground.feature.settings.SettingsScreen
import com.github.diegoberaldin.commonground.feature.settings.configsources.ConfigSourcesScreen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val uiTheme = if (isSystemInDarkTheme()) {
                UiTheme.Dark
            } else {
                UiTheme.Light
            }
            val themeRepository = rememberByInjection<ThemeRepository>()
            val navController = rememberNavController()
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val drawerCoordinator = rememberByInjection<DrawerCoordinator>()
            val coroutineScope = rememberCoroutineScope()
            val drawerGesturesEnabled by drawerCoordinator.gesturesEnabled.collectAsState()

            LaunchedEffect(themeRepository) {
                themeRepository.changeTheme(uiTheme)
            }

            CommonGroundTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    fun toggleDrawer() {
                        drawerState.apply {
                            coroutineScope.launch {
                                if (isClosed) {
                                    open()
                                } else {
                                    close()
                                }
                            }
                        }
                    }

                    LaunchedEffect(drawerCoordinator) {
                        drawerCoordinator.events.onEach { event ->
                            when (event) {
                                DrawerEvent.Toggle -> toggleDrawer()
                            }

                        }.launchIn(this)
                        drawerCoordinator.section.onEach { section ->
                            when (section) {
                                is DrawerSection.ImageList -> {
                                    navController.popUntilRoot()
                                    navController.navigate(
                                        route = NavigationDestination.Home.route
                                    )
                                }

                                DrawerSection.Favorites -> {
                                    navController.popUntilRoot()
                                    navController.navigate(
                                        route = NavigationDestination.Favorites.route
                                    )
                                }

                                DrawerSection.Settings -> {
                                    navController.popUntilRoot()
                                    navController.navigate(
                                        route = NavigationDestination.Settings.Main.route,
                                    )
                                }

                                else -> {
                                    Unit
                                }
                            }
                        }.launchIn(this)
                    }

                    ModalNavigationDrawer(
                        gesturesEnabled = drawerGesturesEnabled,
                        drawerState = drawerState,
                        drawerContent = {
                            ModalDrawerSheet {
                                DrawerContent(
                                    modifier = Modifier.padding(
                                        vertical = Spacing.m,
                                    ),
                                )
                            }
                        },
                        content = {
                            NavHost(
                                navController = navController,
                                startDestination = NavigationDestination.Home.route,
                            ) {
                                buildNavGraph(
                                    navController = navController,
                                )
                            }
                        },
                    )
                }
            }
        }
    }
}

private fun NavGraphBuilder.buildNavGraph(
    navController: NavHostController,
) {
    composable(NavigationDestination.Home.route) {
        ImageListScreen(
            onOpenDetail = { imageId ->
                navController.navigate(
                    route = NavigationDestination.ImageDetail.withActualParams(
                        params = mapOf("id" to imageId)
                    )
                )
            },
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
    composable(
        route = NavigationDestination.Favorites.route,
    ) {
        FavoritesScreen(
            onOpenDetail = { imageId ->
                navController.navigate(
                    route = NavigationDestination.ImageDetail.withActualParams(
                        params = mapOf("id" to imageId)
                    )
                )
            },
        )
    }
    navigation(
        startDestination = NavigationDestination.Settings.Main.route,
        route = NavigationDestination.Settings.Root.route,
    ) {

    }
    composable(
        route = NavigationDestination.Settings.Main.route,
    ) {
        SettingsScreen(
            onConfigSources = {
                navController.navigate(route = NavigationDestination.Settings.ConfigureSources.route)
            },
        )
    }
    composable(
        route = NavigationDestination.Settings.ConfigureSources.route,
    ) {
        ConfigSourcesScreen(
            onBack = {
                navController.popBackStack()
            },
        )
    }
}

private fun NavHostController.popUntilRoot() {
    var empty = false
    while (!empty) {
        empty = !popBackStack()
    }
}