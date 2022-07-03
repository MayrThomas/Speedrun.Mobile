package at.fhhagenberg.me.ada.speedrunmobile.network

interface SpeedrunProxy {
    suspend fun getGames(filter: String? = null, page: Int): MutableCollection<Any>?
    suspend fun getRuns(gameId: String)
    suspend fun getRun(runId: String)
    suspend fun getRunner(runnerId: String)
}