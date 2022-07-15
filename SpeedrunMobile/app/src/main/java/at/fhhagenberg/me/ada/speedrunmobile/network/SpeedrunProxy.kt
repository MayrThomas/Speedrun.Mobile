package at.fhhagenberg.me.ada.speedrunmobile.network

import at.fhhagenberg.me.ada.speedrunmobile.core.Game
import at.fhhagenberg.me.ada.speedrunmobile.core.Run
import at.fhhagenberg.me.ada.speedrunmobile.core.Runner

interface SpeedrunProxy {
    suspend fun getGames(filter: String? = null, page: Int): MutableCollection<Game>?
    suspend fun getGame(gameId: String): Game?
    suspend fun getRuns(gameId: String, categoryId: String): MutableCollection<Run>?
    suspend fun getRunner(runnerId: String): Runner?
}