package com.dishcovery.app

import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

data class RecipeShort(
    val id: Int,
    val title: String,
    @Json(name = "primary_category") val category: String,
    @Json(name = "image_url") val imageUrl: String,
    @Json(name = "has_video") val hasVideo: Boolean
)

data class RecipeFull(
    val id: Int,
    val title: String,
    val description: String = "",
    @Json(name = "primary_category") val category: String,
    @Json(name = "image_url") val imageUrl: String,
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<RecipeStep> = emptyList(),
    val author: String = ""
)

data class Ingredient(val amount: String? = null, val name: String? = null)
data class RecipeStep(@Json(name = "step_label") val label: String, val text: String)
data class RecipePage(val items: List<RecipeShort>, val total: Int)
data class SearchResponse(val items: List<RecipeShort>)

interface OshxonaApi {
    @GET("api/v1/recipes/") suspend fun recipes(@Query("lang") language: String = "uz", @Query("page") page: Int = 0, @Query("per_page") perPage: Int = 20): RecipePage
    @GET("api/v1/recipes/{id}") suspend fun recipe(@Path("id") id: Int): RecipeFull
    @GET("api/v1/search/") suspend fun search(@Query("q") query: String, @Query("lang") language: String = "uz"): SearchResponse
}

object OshxonaService {
    private val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
    val api: OshxonaApi = Retrofit.Builder()
        .baseUrl("https://oshxona-api.zokirov-mob-dev.uz/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(OshxonaApi::class.java)
}
