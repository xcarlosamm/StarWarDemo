package co.swapi.demo.nio.api

import android.util.Log
import co.swapi.demo.BuildConfig
import co.swapi.demo.model.Film
import co.swapi.demo.model.People
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


private const val TAG = "StarWarsAPIService"

fun findFilmsByName(
    api: StarWarsAPIService,
    q: String,
    page: Int,
    itemsPerPage: Int = 20,
    onSuccess: (films: List<Film>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "target: Films, query: $q, page: $page, itemsPerPage: $itemsPerPage")

    api.findFilmsByName(q, page).enqueue(
        object : Callback<PagedList<Film>> {
            override fun onFailure(call: Call<PagedList<Film>>?, t: Throwable) {
                Log.d(TAG, "Fail to get data (films)")
                onError(t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<PagedList<Film>>?, response: Response<PagedList<Film>>) {
                Log.d(TAG, "Got a response $response")
                if (response.isSuccessful) {
                    onSuccess(response.body()?.results ?: emptyList())
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun findFilmById(
    api: StarWarsAPIService,
    id: Long,
    onSuccess: (Film, List<Long>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "get filmWithCharacters by id: $id")

    api.getFilm(id).enqueue(
        object : Callback<Film> {
            override fun onFailure(call: Call<Film>?, t: Throwable) {
                Log.d(TAG, "Fail to get data (films)")
                onError(t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<Film>?, response: Response<Film>) {
                Log.d(TAG, "Got a response $response")
                if (response.isSuccessful) {
                    val film = response.body()
                    onSuccess(film!!, film.characterIds ?: emptyList())
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun findPeopleByName(
    api: StarWarsAPIService,
    q: String,
    page: Int,
    itemsPerPage: Int = 20,
    onSuccess: (peoples: List<People>) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "target: People, query: $q, page: $page, itemsPerPage: $itemsPerPage")

    api.findPeopleByName(q, page).enqueue(
        object : Callback<PagedList<People>> {
            override fun onFailure(call: Call<PagedList<People>>?, t: Throwable) {
                Log.d(TAG, "Fail to get data (people)")
                onError(t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<PagedList<People>>?, response: Response<PagedList<People>>) {
                Log.d(TAG, "Got a response $response")
                if (response.isSuccessful) {
                    onSuccess(response.body()?.results ?: emptyList())
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

fun findPeopleById(
    api: StarWarsAPIService,
    id: Long,
    onSuccess: (people: People) -> Unit,
    onError: (error: String) -> Unit
) {
    Log.d(TAG, "get people by id: $id")

    api.getCharacter(id).enqueue(
        object : Callback<People> {
            override fun onFailure(call: Call<People>?, t: Throwable) {
                Log.d(TAG, "Fail to get data (people)")
                onError(t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<People>?, response: Response<People>) {
                Log.d(TAG, "Got a response $response")
                if (response.isSuccessful) {
                    onSuccess(response.body()!!)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

/**
 * Star Wars API service (https://swapi.co/)
 */
interface StarWarsAPIService {

    @GET("people/{id}/")
    fun getCharacter(@Path("id") id: Long): Call<People>

    @GET("people/")
    fun findPeopleByName(@Query("search") q: String = "", @Query("page") page: Int = 1): Call<PagedList<People>>


    @GET("films/{id}/")
    fun getFilm(@Path("id") id: Long): Call<Film>

    @GET("films/")
    fun findFilmsByName(@Query("search") q: String = "", @Query("page") page: Int = 1): Call<PagedList<Film>>

    companion object {
        private const val BASE_URL = "https://swapi.co/api/"

        private fun create(): StarWarsAPIService {

            val logger = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }

            val client = OkHttpClient().newBuilder()
                .addInterceptor(logger)
                .build()

            val gson = GsonBuilder()
                //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") // ISO 8601
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(StarWarsAPIService::class.java)
        }

        // For Singleton instantiation
        @Volatile private var instance: StarWarsAPIService? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: create().also { instance = it }
            }
    }
}