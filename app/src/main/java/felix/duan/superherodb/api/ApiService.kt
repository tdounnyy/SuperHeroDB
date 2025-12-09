package felix.duan.superherodb.api

import android.util.Log
import com.google.gson.GsonBuilder
import felix.duan.superherodb.model.ApiResponseTypeAdapterFactory
import felix.duan.superherodb.model.SearchResultData
import felix.duan.superherodb.model.SuperHeroData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    private const val TAG: String = "SuperHeroEndpoint"

    private const val BASE_URL = "https://superheroapi.com/"

    private val api: SuperHeroApi by lazy { buildAPI() }

    suspend fun getById(id: String): Result<SuperHeroData> {
        // TODO: 2025/12/9 (duanyufei) id validation
        val resp = api.getById(id = id)
        if (resp.errorMsg != null && resp.errorMsg.isNotEmpty()) {
            val msg = "API Error: getById ${resp.errorMsg}"
            Log.e(TAG, msg)
            return Result.failure(Exception(msg))
        }
        return Result.success(resp.data as SuperHeroData)
    }

    suspend fun searchByName(name: String): Result<SearchResultData> {
        // TODO: 2025/12/9 (duanyufei) name validation
        val resp = api.searchByName(name = name)
        if (resp.errorMsg != null && resp.errorMsg.isNotEmpty()) {
            val msg = "API Error: searchByName ${resp.errorMsg}"
            Log.e(TAG, msg)
            return Result.failure(Exception(msg))
        }
        return Result.success(resp.data as SearchResultData)
    }

    private fun buildAPI(): SuperHeroApi {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        val gson = GsonBuilder()
            .registerTypeAdapterFactory(ApiResponseTypeAdapterFactory())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(SuperHeroApi::class.java)
    }
}
