package com.github.diegoberaldin.commonground

internal sealed class NavigationDestination(val route: String) {
    data object Home : NavigationDestination("imageList")

    data object ImageDetail : NavigationDestination("image/{id}")

    data object Favorites : NavigationDestination("favorites")
    data object Settings : NavigationDestination("settings")

    fun withActualParams(params: Map<String, Any>): String {
        var res = route
        for (entry in params) {
            res = res.replace("{${entry.key}}", entry.value.toString())
        }
        return res
    }
}
