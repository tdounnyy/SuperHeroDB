package felix.duan.superherodb.api

import android.util.Log
import felix.duan.superherodb.model.SearchResultData
import felix.duan.superherodb.model.SuperHeroData
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SuperHeroEndpoint {

    private const val TAG: String = "SuperHeroEndpoint"

    private const val BASE_URL = "https://superheroapi.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(globalOkHttpClient.newBuilder().addInterceptor(loggingInterceptor).build())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val superHeroApi: SuperHeroApi by lazy {
        retrofit.create(SuperHeroApi::class.java)
    }

    suspend fun getById(id: String): Result<SuperHeroData> {
        // TODO: 2025/12/9 (duanyufei) id validation
        val resp = superHeroApi.getById(id = id)
        if (resp.error != null && resp.error.isNotEmpty()) {
            val msg = "API Error: getById ${resp.error}"
            Log.e(TAG, msg)
            return Result.failure(Exception(msg))
        }
        return Result.success(resp)
    }

    suspend fun searchByName(name: String): Result<SearchResultData> {
        // TODO: 2025/12/9 (duanyufei) name validation
        val resp = superHeroApi.searchByName(name = name)
        if (resp.error != null && resp.error.isNotEmpty()) {
            val msg = "API Error: searchByName ${resp.error}"
            Log.e(TAG, msg)
            return Result.failure(Exception(msg))
        }
        return Result.success(resp)
    }

}
