package at.fhhagenberg.me.ada.speedrunmobile.screens

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import at.fhhagenberg.me.ada.speedrunmobile.core.Category
import at.fhhagenberg.me.ada.speedrunmobile.core.Game

@Composable
fun GameScreen(game: Game) {
    game.id?.let { Text(it) }
}


@Composable
fun CategoryNavigationBar(categories: List<Category>) {

}