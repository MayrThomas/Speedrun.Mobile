package at.fhhagenberg.me.ada.speedrunmobile

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import at.fhhagenberg.me.ada.speedrunmobile.screens.Drawer
import at.fhhagenberg.me.ada.speedrunmobile.screens.Favorites
import at.fhhagenberg.me.ada.speedrunmobile.screens.GameScreen
import at.fhhagenberg.me.ada.speedrunmobile.screens.Home
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.SpeedrunMobileTheme
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext
import kotlin.math.log

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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
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
                openDrawer = { openDrawer() }, viewModel)
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
) {
    val context = LocalContext.current
    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            Drawer(
                onDestinationClicked = { game, category ->
                    viewModel.categoryChanged = true
                    scope.launch {
                        drawerState.close()
                    }
                    //viewModel.onCurrentCategoryChanged(category)
                    navigateToGame(navController,
                        game,
                        category,
                        viewModel,
                        context,
                        context.getString(R.string.categories_unavailable))
                }, game = viewModel.currentGame)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Home.route,
        ) {
            composable(NavRoutes.Home.route) {
                Home(onGameClicked = { gameID ->
                    navigateToGame(navController,
                        gameID,
                        UNDEFINED_CATEGORY,
                        viewModel,
                        context,
                        context.getString(R.string.categories_unavailable))
                }, viewModel = viewModel)
            }

            composable(NavRoutes.Favorites.route) {
                Favorites(onGameClicked = { gameID ->
                    navigateToGame(navController,
                        gameID,
                        UNDEFINED_CATEGORY,
                        viewModel,
                        context,
                        context.getString(R.string.categories_unavailable))
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
                )) {

                if (viewModel.categoryChanged) {
                    viewModel.updateRuns()
                }

                GameScreen(
                    openDrawer = { openDrawer() }, viewModel = viewModel)
            }
        }
    }
}

private fun navigateToGame(
    navController: NavHostController,
    gameID: String,
    categoryID: String,
    viewModel: SMViewModel,
    context: Context,
    failMessage: String,
) {
    viewModel.onCurrentGameChanged(gameID)
    if (viewModel.currentGame.categories?.isNotEmpty() == true) {
        viewModel.categoryChanged = true
        viewModel.onCurrentCategoryChanged(categoryID)
        navController.navigate("${NavRoutes.Games.route}/$gameID/$categoryID") {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    } else {
        showToast(context, failMessage)
    }
}

private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
        MainScreen()
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DefaultPreviewDark() {
    SpeedrunMobileTheme {
        MainScreen()
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: SMViewModel = viewModel(),
) {
    var value: String by rememberSaveable { mutableStateOf("") }
    TextField(
        value = value,
        onValueChange = { value = it },
        textStyle = MaterialTheme.typography.h6,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        trailingIcon = {
            if(viewModel.showingSearchResult){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.clickable { viewModel.onGamesSearchEnd() }
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null,
                    modifier = Modifier.clickable { viewModel.onGamesSearch(value) }
                )
            }
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