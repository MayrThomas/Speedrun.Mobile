package at.fhhagenberg.me.ada.speedrunmobile.viewmodel

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import at.fhhagenberg.me.ada.speedrunmobile.core.Game
import at.fhhagenberg.me.ada.speedrunmobile.network.SpeedrunProxyFactory
import kotlinx.coroutines.*

class SMViewModel : ViewModel() {
    private val _games = listOf<Game>().toMutableStateList()
    val games: List<Game> get() = _games

    private val _favourites = listOf<Game>().toMutableStateList()
    val favourites: List<Game> get() = _favourites


    private fun addFavourite(game: Game){
        _favourites.add(game)
    }

    private fun removeFavourite(game: Game){
        _favourites.remove(game)
    }

    fun onFavouriteChanged(game: Game, fav: Boolean){
        games.find { it.id == game.id }!!.fav = fav
        if(fav){
            addFavourite(game)
        } else {
            removeFavourite(game)
        }
    }

    fun initGames(){
        fetchGames(onGamesReceived = {list ->
            _games.addAll(list)
        })
    }

    private fun fetchGames(onGamesReceived: (List<Game>) -> Unit){
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
}

