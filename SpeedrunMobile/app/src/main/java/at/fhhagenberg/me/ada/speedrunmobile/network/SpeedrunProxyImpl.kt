package at.fhhagenberg.me.ada.speedrunmobile.network

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

    override suspend fun getGames(filter: String?, page: Int): MutableCollection<Any>? {
        val gamesObject = proxy.getGames(filter, page*20).execute().body() ?: return null
        return mutableListOf()
    }

    override suspend fun getRuns(gameId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRun(runId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getRunner(runnerId: String) {
        TODO("Not yet implemented")
    }


    //TODO("change return value of network calls to objects that contain a list of the actual type")
    private interface Proxy {
        @GET("games")
        fun getGames(@Query("name") name: String?, @Query("offset") offset: Int): Call<Proxy_GameObject>

        @GET("runs")
        fun getRuns(@Query("game") gameId: String): Call<String>

        @GET("runs/{id}")
        fun getRun(@Path("id") runId: String): Call<String>

        @GET("users/{id}")
        fun getRunner(@Path("id") runner: String): Call<String>
    }

    //TODO("create names object to store game names")
    private class Proxy_Game {
        var id: String? = null
        var names: Proxy_GameNames? = null
        var released: Int? = null
    }

    private class Proxy_GameObject {
        var data: List<Proxy_Game>? = null
    }

    private class Proxy_GameNames {
        val international: String? = null
        val japanese: String? = null
        val twitch: String? = null
    }
}