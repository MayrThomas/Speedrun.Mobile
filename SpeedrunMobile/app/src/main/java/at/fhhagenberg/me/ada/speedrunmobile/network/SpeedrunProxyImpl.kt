package at.fhhagenberg.me.ada.speedrunmobile.network

import at.fhhagenberg.me.ada.speedrunmobile.core.*
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class SpeedrunProxyImpl : SpeedrunProxy {

    private val BASE_URL = "https://www.speedrun.com/api/v1/"

    private val proxy: Proxy = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(Proxy::class.java)

    override suspend fun getGames(filter: String?, page: Int): MutableCollection<Game>? {
        val gamesObject = proxy.getGames(filter, page * 20).execute().body() ?: return null
        val gamesList = gamesObject.data?.map {
            it.toGame()
        }?.toMutableList()

        gamesList?.forEach {
            val categories = proxy.getCategoriesOfGame(it.id).execute().body()
            if (categories != null) {
                it.categories = categories.data?.map {
                    it.toCategory()
                }
            }
        }

        return gamesList
    }

    override suspend fun getGame(gameId: String): Game? {
        val gamesObject = proxy.getGame(gameId).execute().body() ?: return null
        val game = gamesObject.data?.toGame()
        if(game?.id != null) {
            val categories = proxy.getCategoriesOfGame(game.id).execute().body()
            if (categories != null) {
                game.categories = categories.data?.map {
                    it.toCategory()
                }
            }
        }
        return game
    }

    override suspend fun getRuns(gameId: String, categoryId: String): MutableCollection<Run>? {
        val leaderboardObject = proxy.getRuns(gameId, categoryId).execute().body() ?: return null

        return leaderboardObject.data?.runs?.map { it.toRun() }?.toMutableList()
    }

    override suspend fun getRunner(runnerId: String): Runner? {
        val userObject = proxy.getRunner(runnerId).execute().body() ?: return null

        return userObject.data?.toRunner()!!
    }


    //TODO("change return value of network calls to objects that contain a list of the actual type")
    private interface Proxy {
        @GET("games")
        fun getGames(
            @Query("name") name: String?,
            @Query("offset") offset: Int,
        ): Call<Proxy_GameObjects>

        @GET("games/{id}")
        fun getGame(@Path("id") gameId: String?): Call<Proxy_GameObject>

        @GET("games/{id}/categories")
        fun getCategoriesOfGame(@Path("id") gameId: String?): Call<Proxy_CategoryObject>

        @GET("leaderboards/{game}/category/{category}")
        fun getRuns(
            @Path("game") gameId: String,
            @Path("category") categoryId: String,
        ): Call<Proxy_LeaderboardObject>

        @GET("users/{id}")
        fun getRunner(@Path("id") runner: String): Call<Proxy_UserObject>
    }

    private class Proxy_Game {
        var id: String? = null
        var names: Proxy_GameNames? = null
        var released: Int? = null
        var assets: Proxy_GameAssets? = null

        fun toGame(): Game {
            return Game(id, released, names?.toGameNames(), assets?.cover_medium?.uri)
        }
    }

    private class Proxy_GameObjects {
        var data: List<Proxy_Game>? = null
    }

    private class Proxy_GameObject{
        var data: Proxy_Game? = null
    }

    private class Proxy_GameNames {
        val international: String? = null
        val japanese: String? = null
        val twitch: String? = null

        fun toGameNames(): GameNames {
            return GameNames(international, japanese, twitch)
        }

        fun toListOfNames(): List<String?> {
            return listOf(international, japanese, twitch)
        }
    }

    private class Proxy_CategoryObject {
        var data: List<Proxy_Category>? = null
    }

    private class Proxy_Logo {
        val uri: String? = null
    }

    private class Proxy_Category {
        var id: String? = null
        var name: String? = null
        var rules: String? = null

        fun toCategory(): Category {
            return Category(id, name, rules)
        }
    }

    private class Proxy_LeaderboardObject {
        var data: Proxy_Leaderboard? = null
    }

    private class Proxy_Leaderboard {
        var runs: List<Proxy_RunsObject>? = null
    }

    private class Proxy_RunsObject {
        var place: Int? = null
        var run: Proxy_Run? = null

        fun toRun(): Run {
            return Run(
                run?.id,
                place,
                run?.comment,
                run?.videos?.toURIList(),
                run?.players?.map { it.id },
                run?.submitted,
                run?.times
            )
        }
    }

    private class Proxy_Run {
        var id: String? = null
        var comment: String? = null
        var submitted: String? = null
        var times: Proxy_Time? = null
        var videos: Proxy_Video? = null
        var players: List<Proxy_Players>? = null
    }

    private class Proxy_Time {
        var primary: String? = null
        var realtime: String? = null
        var ingame: String? = null
    }

    private class Proxy_Video {
        var links: List<Proxy_Link>? = null

        fun toURIList(): List<String?>? {
            return links?.map {
                it.uri
            }?.toList()
        }
    }

    private class Proxy_Link {
        var uri: String? = null
    }

    private class Proxy_Players {
        var id: String? = null
    }

    private class Proxy_UserObject {
        var data: Proxy_User? = null
    }

    private class Proxy_User {
        var id: String? = null
        var names: Proxy_GameNames? = null
        var pronouns: String? = null
        var twitch: Proxy_Link? = null
        var youtube: Proxy_Link? = null
        var twitter: Proxy_Link? = null
        var location: Proxy_Location? = null
        var assets: Proxy_Assets? = null

        fun toRunner(): Runner {
            return Runner(
                id,
                names?.toListOfNames(),
                pronouns,
                twitch?.uri,
                youtube?.uri,
                twitter?.uri,
                location?.country?.code,
                assets?.icon?.uri
            )
        }
    }

    private class Proxy_Country {
        var code: String? = null
    }

    private class Proxy_Location {
        var country: Proxy_Country? = null
    }

    private class Proxy_Assets {
        var icon: Proxy_Link? = null
    }

    private class Proxy_GameAssets {
        @SerializedName("cover-medium")
        var cover_medium: Proxy_Link? = null
    }
}