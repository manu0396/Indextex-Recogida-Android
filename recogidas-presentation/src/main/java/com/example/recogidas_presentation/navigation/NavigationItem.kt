package com.example.recogidas_presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.recogidas_presentation.R

sealed class NavigationItem(
    val route: String,
    val labelRes: Int,
    val icon: ImageVector,
) {
    object Home : NavigationItem("home", R.string.nav_home, Icons.Default.Home)
    object Favorites : NavigationItem("favs", R.string.nav_favs, Icons.Default.Favorite)
    companion object {
        val entries = listOf(Home, Favorites)
    }
}
