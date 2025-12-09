package felix.duan.superherodb.api

import felix.duan.superherodb.model.SearchResultData
import felix.duan.superherodb.model.SuperHeroData
import okhttp3.OkHttpClient
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

// TODO: 2025/12/9 (duanyufei) DEBUG ACCESS TOKEN
const val ACCESS_TOKEN = "0eb6582d9082ae358383d679ea23538f"

interface SuperHeroApi {
    @GET("api/${ACCESS_TOKEN}/{id}")
    suspend fun getById(@Path("id") id: String): SuperHeroData

    @GET("api/${ACCESS_TOKEN}/search/{name}")
    suspend fun searchByName(@Path("name") name: String): SearchResultData
}

// Global OkHttpClient
val globalOkHttpClient by lazy {
    OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
}