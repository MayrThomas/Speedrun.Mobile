package at.fhhagenberg.me.ada.speedrunmobile.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import at.fhhagenberg.me.ada.speedrunmobile.PREFERRED_BOTTOM_NAV_HEIGHT
import at.fhhagenberg.me.ada.speedrunmobile.SearchBar
import at.fhhagenberg.me.ada.speedrunmobile.core.Game
import at.fhhagenberg.me.ada.speedrunmobile.network.SpeedrunProxyFactory
import at.fhhagenberg.me.ada.speedrunmobile.ui.theme.HeaderGreen
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Home(onGameClicked: (String) -> Unit, viewModel: SMViewModel) {
    val coroutineScope = rememberCoroutineScope()

    /*val games = remember { mutableStateListOf<Game>() }

    coroutineScope.launch {
        withContext(Dispatchers.IO) {
            val proxyGames = SpeedrunProxyFactory.createProxy().getGames(page = 0)

            withContext(Dispatchers.Main) {
                if (proxyGames != null) {
                    games.addAll(proxyGames)
                }
            }
        }
    }*/

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))

        GamesBody(modifier = Modifier.padding(top = 16.dp),
            data = viewModel.games,
            onGameClicked = onGameClicked,
            onFavouriteChanged = { game, fav ->
                viewModel.onFavouriteChanged(game, fav)
            })

    }
}

@Composable
fun GamesBody(
    modifier: Modifier = Modifier,
    onGameClicked: (String) -> Unit,
    data: List<Game> = listOf(),
    onFavouriteChanged: (Game, Boolean) -> Unit,
) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = PREFERRED_BOTTOM_NAV_HEIGHT.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = modifier) {
        items(data) { item ->
            Game(data = item, onGameClicked = onGameClicked, onFavouriteChanged = { fav ->
                onFavouriteChanged(item,fav)
            })
        }
    }
}

@Composable
fun Game(
    modifier: Modifier = Modifier,
    data: Game?,
    onGameClicked: (String) -> Unit,
    onFavouriteChanged: (Boolean) -> Unit,
) {

    Surface(color = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(Dp.Hairline,
            HeaderGreen),
        modifier = modifier.clickable { onGameClicked(data?.id!!) }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(horizontalArrangement = Arrangement.Start) {
                IconButton(onClick = { onFavouriteChanged(!data?.fav!!) }) {
                    if (data?.fav!!) {
                        Icon(Icons.Default.Favorite, contentDescription = null)

                    } else {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = null)
                    }
                }
                AsyncImage(model = data?.cover,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(top = 8.dp))
            }
            data?.names?.international?.let {
                Text(text = it,
                    modifier = Modifier.padding(bottom = 8.dp, start = 4.dp, end = 4.dp
                    ))
            }
        }
    }
}