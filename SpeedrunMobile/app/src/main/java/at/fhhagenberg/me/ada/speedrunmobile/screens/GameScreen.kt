package at.fhhagenberg.me.ada.speedrunmobile.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.fhhagenberg.me.ada.speedrunmobile.PREFERRED_BOTTOM_NAV_HEIGHT
import at.fhhagenberg.me.ada.speedrunmobile.R
import at.fhhagenberg.me.ada.speedrunmobile.core.*
import at.fhhagenberg.me.ada.speedrunmobile.core.Run
import at.fhhagenberg.me.ada.speedrunmobile.network.SpeedrunProxyFactory
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.SpeedrunMobileTheme
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    viewModel.categoryChanged = false
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

@SuppressLint("SimpleDateFormat")
@Composable
fun Run(run: Run, onPlayClicked: (String) -> Unit) {
    val color =
        if (run.place!! % 2 == 0) MaterialTheme.colors.primary else MaterialTheme.colors.primaryVariant
    Surface(color = color) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val fulldate = dateFormatter.parse(run.submitted)
        val smalldate = SimpleDateFormat("yyyy-MM-dd', 'HH:mm").format(fulldate)
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Text(text = run.place.toString(), modifier = Modifier.fillMaxWidth(0.1f), textAlign = TextAlign.Center)
            Text(text = run.actualRunner?.names?.first() ?: "Player",
                modifier = Modifier
                    .clickable { }
                    .fillMaxWidth(0.4f))
            Text(text = smalldate, modifier = Modifier.fillMaxWidth(0.9f))
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
    val scrollState = rememberScrollState()
    Column(
        modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 48.dp)
            .scrollable(state = scrollState, orientation = Orientation.Vertical)
    ) {
        Card(shape = MaterialTheme.shapes.large) {
            AsyncImage(model = game?.cover,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                alignment = Alignment.Center)
        }
        game?.categories?.forEach { category ->
            Spacer(Modifier.height(24.dp))
            Text(
                text = category.name!!,
                style = MaterialTheme.typography.h5,
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

private val testCategories: List<Category> = listOf(
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

