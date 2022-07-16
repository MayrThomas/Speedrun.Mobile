package at.fhhagenberg.me.ada.speedrunmobile.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class Game(
    val id: String?,
    val released: Int?,
    val names: GameNames?,
    val cover: String?,
    var categories: List<Category>? = null,
    initialFav: Boolean = false
){
    var fav by mutableStateOf(initialFav)
}
