package at.fhhagenberg.me.ada.speedrunmobile.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import at.fhhagenberg.me.ada.speedrunmobile.PREFERRED_BOTTOM_NAV_HEIGHT
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
import java.util.*

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
    viewModel: SMViewModel,
    onPlayClicked: (String) -> Unit
) {
    Scaffold(topBar = {
        CategoryNavigationBar(categories = viewModel.currentGame.categories,
            openDrawer = { openDrawer() }, currentCategoryID = viewModel.currentCategory)
    }) {
        RunsBody(runs = viewModel.runs, Modifier.padding(bottom = PREFERRED_BOTTOM_NAV_HEIGHT.dp), onPlayClicked)
    }


}

@Composable
fun RunsBody(runs: List<Run>, modifier: Modifier = Modifier, onPlayClicked: (String) -> Unit = {}) {
    LazyColumn(horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp), modifier = modifier) {
        items(runs) { run ->
            Run(run, onPlayClicked)
        }
    }
}

@Composable
fun Run(run: Run, onPlayClicked: (String) -> Unit) {
    val color =
        if (run.place!! % 2 == 0) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
    Surface(color = color) {

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = run.place.toString(), modifier = Modifier.fillMaxWidth(0.1f))
            Text(text = run.runners?.first() ?: "Player",
                modifier = Modifier
                    .clickable { /*TODO navigate to runner*/ }
                    .fillMaxWidth(0.4f))
            //Text(text = run.times.toString())
            Text(text = run.submitted ?: "Date", modifier = Modifier.fillMaxWidth(0.9f))
            IconButton(onClick = { onPlayClicked(run.videos?.get(0)!!) }, modifier = Modifier.fillMaxWidth(1f)) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Video of run")
            }
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

private val testRuns = listOf(
    Run(
        "zp9p4j8m",
        1,
        "First game in alphabetic order on speedrun.com.\r\nThank you for verifying  f1shy ! ",
        null,
        listOf("kj9p77x4"),
        "2022-06-18T10:27:58Z",
        null
    ),
    Run(
        "yvqll06m",
        2,
        null,
        null,
        listOf("zx7ero68"),
        "2022-02-14T22:35:29Z",
        null
    ),
    Run(
        "yvqk21xm",
        3,
        "Using: Flashpoint 10.1.0.5",
        null,
        listOf("xk3y5l98"),
        "2022-03-17T15:29:22Z",
        null
    )
)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, widthDp = 400)
@Composable
fun RunPreviewDark() {
    SpeedrunMobileTheme {
        RunsBody(runs = testRuns)
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
fun RunPreviewLight() {
    SpeedrunMobileTheme {
        RunsBody(runs = testRuns)
    }
}

