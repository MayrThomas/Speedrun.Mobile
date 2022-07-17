package at.fhhagenberg.me.ada.speedrunmobile.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.fhhagenberg.me.ada.speedrunmobile.R
import at.fhhagenberg.me.ada.speedrunmobile.core.Category
import at.fhhagenberg.me.ada.speedrunmobile.core.Game
import at.fhhagenberg.me.ada.speedrunmobile.core.Run
import at.fhhagenberg.me.ada.speedrunmobile.core.UNDEFINED_CATEGORY
import at.fhhagenberg.me.ada.speedrunmobile.network.SpeedrunProxyFactory
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.SpeedrunMobileTheme
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val testCategories: List<Category> = listOf(
    Category(id = "02qr00pk",
        name = "Any%",
        rules = "Any% - Reach the credits of the game. \\r\\n\\r\\n* The run stops on credits, not the final cutscene. You must skip the final cutscene before getting your final time. \\r\\n* If you use the save \\u0026 quit mechanic at any point during a run, any delay in loading your character will cause a run to get rejected.\\r\\n\\r\\n##Chinese Translation \\u4e2d\\u6587\\u7ffb\\u8bd1\\r\\nhttps://www.bilibili.com/read/cv15826478"),
    Category(id = "9kvnee8d",
        name = "Any% Unrestricted",
        rules = "Any% Unrestricted - Reach the credits of the game. \\r\\n\\r\\n* The run stops on credits, not the final cutscene. You must skip the final cutscene before getting your final time. \\r\\n* Force quitting the game via Alt+F4 is allowed for executing wrong warps. \\r\\n* Zips are allowed. \\r\\n* If you use the save \\u0026 quit mechanic at any point during a run, any delay in loading your character will cause a run to get rejected.\\r\\n\\r\\n##Chinese Translation \\u4e2d\\u6587\\u7ffb\\u8bd1\\r\\nhttps://www.bilibili.com/read/cv15826478"),
    Category(id = "mkey0882",
        name = "Any% No Wrong Warp",
        rules = "Any% No Wrong Warp - Reach the credits of the game. The use of the wrong warp and \\u0022Pegasus\\u0022 glitches are banned.\\r\\n\\r\\n* The run stops on credits, not the final cutscene. You must skip the final cutscene before getting your final time.\\r\\n* If you use the save \\u0026 quit mechanic at any point during a run, any delay in loading your character will cause a run to get rejected.\\r\\n\\r\\n##Chinese Translation \\u4e2d\\u6587\\u7ffb\\u8bd1\\r\\nhttps://www.bilibili.com/read/cv15826478")
)

@SuppressLint("CoroutineCreationDuringComposition", "UnrememberedMutableState")
@Composable
fun GameScreen(
    openDrawer: () -> Unit,
    viewModel: SMViewModel
) {
    CategoryNavigationBar(categories = viewModel.currentGame.categories,
        openDrawer = { openDrawer() }, currentCategoryID = viewModel.currentCategory)

    RunsBody(runs = viewModel.runs)
}

@Composable
fun RunsBody(runs: List<Run>) {
    Column {
        runs.forEach { run ->
            run.id?.let { Text(text = it) }
        }
    }
}


@Composable
fun Drawer(
    modifier: Modifier = Modifier,
    game: Game?,
    onDestinationClicked: (game: String, category: String) -> Unit,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_launcher_background),
            contentDescription = "App icon"
        )
        game?.categories?.forEach { category ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = category.name!!,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.clickable {
                    onDestinationClicked(game.id!!, category.id!!)
                }
            )
        }
    }
}


@Composable
fun CategoryNavigationBar(
    categories: List<Category>? = null,
    currentCategoryID: String?,
    openDrawer: () -> Unit,
) {
    var catID = ""
    catID = if (currentCategoryID.equals(UNDEFINED_CATEGORY) && categories?.isNotEmpty() == true) {
        categories.first().id.toString()
    } else {
        currentCategoryID!!
    }
    val categoryName = categories?.find { cat -> cat.id.equals(catID) }?.name
    TopAppBar(
        title = {
            Text(
                text = categoryName ?: "",
                color = Color.White
            )
        },
        navigationIcon = {
            IconButton(onClick = { openDrawer() }) {
                Icon(imageVector = Icons.Filled.Menu, null)
            }
        },

        elevation = 12.dp
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CategoryNavigationBarPreviewDark() {
    SpeedrunMobileTheme {
        CategoryNavigationBar(categories = testCategories,
            openDrawer = {}, currentCategoryID = "Any%")
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryNavigationBarPreviewLight() {
    SpeedrunMobileTheme {
        CategoryNavigationBar(categories = testCategories,
            openDrawer = {}, currentCategoryID = "Any%")
    }
}

