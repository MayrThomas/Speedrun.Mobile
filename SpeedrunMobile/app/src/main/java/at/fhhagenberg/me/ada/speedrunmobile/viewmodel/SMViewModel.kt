package at.fhhagenberg.me.ada.speedrunmobile.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import at.fhhagenberg.me.ada.speedrunmobile.core.Game
import at.fhhagenberg.me.ada.speedrunmobile.core.Run
import at.fhhagenberg.me.ada.speedrunmobile.core.UNDEFINED_CATEGORY
import at.fhhagenberg.me.ada.speedrunmobile.network.SpeedrunProxyFactory
import kotlinx.coroutines.*

class SMViewModel : ViewModel() {
    private val _games = listOf<Game>().toMutableStateList()
    val games: List<Game> get() = _games

    private val _runs = listOf<Run>().toMutableStateList()
    val runs: List<Run> get() = _runs

    private val _favourites = listOf<Game>().toMutableStateList()
    val favourites: List<Game> get() = _favourites

    private val _currentGame = mutableStateOf(Game(null, null, null, null))
    val currentGame: Game get() = _currentGame.value

    private val _currentCategory = mutableStateOf(UNDEFINED_CATEGORY)
    val currentCategory: String get() = _currentCategory.value

    private val _currentVideo = mutableStateOf("")
    val currentVideo: String get() = _currentVideo.value


    fun onCurrentCategoryChanged(newCategoryID: String) {
        if (newCategoryID == UNDEFINED_CATEGORY) {
            _currentCategory.value = _currentGame.value.categories?.first()?.id!!
        } else {
            _currentCategory.value = newCategoryID
        }
    }

    fun onCurrentGameChanged(newGame: Game) {
        _currentGame.value = newGame
    }

    fun onCurrentGameChanged(newGameID: String) {
        _currentGame.value = _games.find { it.id.equals(newGameID) } ?: _currentGame.value
    }

    fun onCurrentVideoChange(newVideo: String) {
        _currentVideo.value = newVideo
    }

    private fun addFavourite(game: Game) {
        _favourites.add(game)
    }

    private fun removeFavourite(game: Game) {
        _favourites.remove(game)
    }

    fun onFavouriteChanged(game: Game, fav: Boolean) {
        games.find { it.id == game.id }!!.fav = fav
        if (fav) {
            addFavourite(game)
        } else {
            removeFavourite(game)
        }
    }

    fun updateGames() {
        fetchGames(onGamesReceived = { list ->
            _games.addAll(list)
        })
    }

    fun updateRuns() {
        fetchRunsToCurrentCategory(onRunsReceived = { list ->
            _runs.removeAll(_runs)
            _runs.addAll(list)
        })
    }

    private fun fetchGames(onGamesReceived: (List<Game>) -> Unit) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            error("Exception: ${throwable.localizedMessage}")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val proxyGames = SpeedrunProxyFactory.createProxy().getGames(page = 0)
            withContext(Dispatchers.Main) {
                onGamesReceived(proxyGames?.toList()!!)
            }
        }
    }

    private fun fetchRunsToCurrentCategory(onRunsReceived: (List<Run>) -> Unit) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            error("Exception: ${throwable.localizedMessage}")
        }
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val proxyRuns =
                SpeedrunProxyFactory.createProxy().getRuns(currentGame.id!!, currentCategory)
            withContext(Dispatchers.Main) {
                onRunsReceived(proxyRuns?.toList()!!)
            }
        }
    }
}

