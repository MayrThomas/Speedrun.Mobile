package at.fhhagenberg.me.ada.speedrunmobile.navigation

sealed class NavRoutes(val route: String) {
    object Home: NavRoutes("home")
    object Favorites: NavRoutes("favorites")
    object Games: NavRoutes("games")
}
