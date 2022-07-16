package at.fhhagenberg.me.ada.speedrunmobile.core

data class Category(
    val id: String?,
    val name: String?,
    val rules: String?
)

const val UNDEFINED_CATEGORY = "undefined_cat"