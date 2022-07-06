package at.fhhagenberg.me.ada.speedrunmobile.core

data class Game(
    val id: String?,
    val released: Int?,
    val names: GameNames?,
    var categories: List<Category>? = null
)
