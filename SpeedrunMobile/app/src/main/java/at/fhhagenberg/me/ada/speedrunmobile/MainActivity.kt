package at.fhhagenberg.me.ada.speedrunmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import at.fhhagenberg.me.ada.speedrunmobile.navigation.NavBarItems
import at.fhhagenberg.me.ada.speedrunmobile.navigation.NavRoutes
import at.fhhagenberg.me.ada.speedrunmobile.screens.Favorites
import at.fhhagenberg.me.ada.speedrunmobile.screens.Home
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.SpeedrunMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpeedrunMobileTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        topBar = { TopAppBar(title = {Text("Bottom Navigation Demo")})  },
        content = { NavigationHost(navController = navController) },
        bottomBar = { BottomNavigationBar(navController = navController)}
    )
}

@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
    ) {
        composable(NavRoutes.Home.route) {
            val names = listOf("Kunal Burrows",
                "Amal Rowley",
                "Sharmin Tanner",
                "Sana Whitney",
                "Howard Fitzpatrick",
                "Sumaiya Cannon",
                "Ross Frazier",
                "Izaac Mills",
                "Wesley Nichols",
                "Areeba Saunders",
                "Kyron Whitley",
                "Sheikh Ware",
                "Misbah Huerta",
                "Tyreese Mccartney",
                "Stella Mcguire",
                "Leland Bauer",
                "Hilary Green",
                "Faizah Kaye",
                "Hanna Cherry",
                "Franklin Monroe",
                "Emmett Brown",
                "Maverick Denton",
                "Alessandra Benson",
                "Julie Thomas",
                "Michael Griffin",
                "Yusuf Whitehouse",
                "Tamera Battle",
                "Keenan Knott",
                "Alaya Wells",
                "Nabil Bray")
            Home(names = names)
        }

        composable(NavRoutes.Favorites.route) {
            Favorites()
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute  = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->

            BottomNavigationItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    Icon(imageVector = navItem.image,
                        contentDescription = navItem.title)
                },
                label = {
                    Text(text = navItem.title)
                },
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeedrunMobileTheme {
        Greeting("Android")
    }
}