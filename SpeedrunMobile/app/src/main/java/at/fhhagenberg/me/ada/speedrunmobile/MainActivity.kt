package at.fhhagenberg.me.ada.speedrunmobile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import at.fhhagenberg.me.ada.speedrunmobile.core.UNDEFINED_CATEGORY
import at.fhhagenberg.me.ada.speedrunmobile.navigation.NavBarItems
import at.fhhagenberg.me.ada.speedrunmobile.navigation.NavRoutes
import at.fhhagenberg.me.ada.speedrunmobile.screens.*
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.SpeedrunMobileTheme
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val context = this.baseContext

        setContent {
            SpeedrunMobileTheme {
                MainScreen(context)
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDrawer = {
        scope.launch {
            drawerState.open()
        }
    }
    val viewModel: SMViewModel = viewModel()
    viewModel.updateGames()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Speedrun.Mobile") }) },
        content = {
            NavigationHost(navController = navController,
                drawerState = drawerState,
                scope = scope,
                openDrawer = { openDrawer() }, viewModel, context)
        },
        bottomBar = { BottomNavigationBar(navController = navController) }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun NavigationHost(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    openDrawer: () -> Unit,
    viewModel: SMViewModel,
    context: Context
) {
    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Drawer(
                onDestinationClicked = { game, category ->
                    scope.launch {
                        drawerState.close()
                    }
                    //viewModel.onCurrentCategoryChanged(category)
                    navigateToGame(navController, game, category, viewModel)
                }, game = viewModel.currentGame)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
        ) {
            composable(NavRoutes.Home.route) {
                Home(onGameClicked = { gameID ->
                    // viewModel.onCurrentGameChanged(gameID)
                    navigateToGame(navController, gameID, UNDEFINED_CATEGORY, viewModel)
                }, viewModel = viewModel)
            }

            composable(NavRoutes.Favorites.route) {
                Favorites(onGameClicked = { gameID ->
                   // viewModel.onCurrentGameChanged(gameID)
                    navigateToGame(navController, gameID, UNDEFINED_CATEGORY, viewModel)
                }, viewModel = viewModel)
            }
            val gameName = NavRoutes.Games.route
            composable(route = "$gameName/{gameID}/{categoryID}",
                arguments = listOf(
                    navArgument("gameID") {
                        type = NavType.StringType
                    },
                    navArgument("categoryID") {
                        type = NavType.StringType
                    }
                )) { entry ->
                val gameID = entry.arguments?.getString("gameID")
                val categoryID = entry.arguments?.getString("categoryID")

                //Gets called twice, probably because it recomposes. Find better way to do this.
                viewModel.updateRuns()

                GameScreen(
                    openDrawer = { openDrawer() }, viewModel = viewModel, onPlayClicked = { videoUrl ->
                        navigateToVideoPlayer(context, videoUrl, viewModel)
                    })
            }
        }
    }
}

private fun navigateToGame(navController: NavHostController, gameID: String, categoryID: String, viewModel: SMViewModel) {
    viewModel.onCurrentGameChanged(gameID)
    viewModel.onCurrentCategoryChanged(categoryID)
    navController.navigate("${NavRoutes.Games.route}/$gameID/$categoryID") {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

private fun navigateToVideoPlayer(context: Context, videoUrl: String, viewModel: SMViewModel) {
    viewModel.onCurrentVideoChange(videoUrl)

    val intent = Intent(ACTION_VIEW)
    intent.data = Uri.parse(videoUrl)
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(context, intent, null)
}

const val PREFERRED_BOTTOM_NAV_HEIGHT = 60
@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation(modifier = Modifier.height(PREFERRED_BOTTOM_NAV_HEIGHT.dp),
        backgroundColor = MaterialTheme.colors.secondary) {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

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


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SpeedrunMobileTheme {
        //MainScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreviewDark() {
    SpeedrunMobileTheme {
        //MainScreen()
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(stringResource(R.string.search), style = MaterialTheme.typography.h6)
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SearchBarPreview() {
    SpeedrunMobileTheme {
        SearchBar()
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreviewLight() {
    SpeedrunMobileTheme {
        SearchBar()
    }
}