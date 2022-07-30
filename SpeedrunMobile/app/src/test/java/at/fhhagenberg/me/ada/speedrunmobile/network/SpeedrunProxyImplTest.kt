package at.fhhagenberg.me.ada.speedrunmobile.network

import at.fhhagenberg.me.ada.speedrunmobile.core.Game
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

internal class SpeedrunProxyImplTest {

    @Test
    fun getGamesTest()  = runBlocking {
        val proxy = SpeedrunProxyFactory.createProxy()

        val data = proxy.getGames(page = 1)
        Assert.assertEquals(data?.size, 20)
    }

    @Test
    fun getSpecificGameTest()  = runBlocking {
        val proxy = SpeedrunProxyFactory.createProxy()

        val data = proxy.getGames("Elden Ring", 0)
        val game = (data as MutableList<Game>)[0]

        Assert.assertEquals(game.names?.international, "Elden Ring")
    }
}