package at.fhhagenberg.me.ada.speedrunmobile.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import at.fhhagenberg.me.ada.speedrunmobile.viewmodel.SMViewModel

@Composable
fun Favorites(onGameClicked: (String) -> Unit, viewModel: SMViewModel) {
    Column {
        GamesBody(modifier = Modifier.padding(top = 16.dp),
            onGameClicked = onGameClicked,
            onFavouriteChanged = { game, fav -> viewModel.onFavouriteChanged(game, fav) },
            data = viewModel.favourites)
    }
}