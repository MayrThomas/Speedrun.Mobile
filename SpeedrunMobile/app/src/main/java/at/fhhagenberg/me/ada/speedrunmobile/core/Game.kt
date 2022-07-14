package at.fhhagenberg.me.ada.speedrunmobile.core

data class Game(
    val id: String?,
    val released: Int?,
    val names: GameNames?,
    val cover: String?,
    var categories: List<Category>? = null
)
