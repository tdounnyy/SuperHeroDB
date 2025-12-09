package felix.duan.superherodb.api

import felix.duan.superherodb.model.SuperHeroData
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiService {

    private const val BASE_URL = "https://cdn.jsdelivr.net/gh/akabab/superhero-api@0.3.0/api/"

    private val api: SuperHeroApi by lazy { buildAPI() }

    suspend fun getAll(): Result<List<SuperHeroData>> {
        return runCatching {
            val resp = api.getAll()
            return Result.success(resp)
        }.onFailure { e ->
            return Result.failure(e)
        }
    }

    suspend fun getById(id: String): Result<SuperHeroData> {
        return runCatching {
            val resp = api.getById(id)
            return Result.success(resp)
        }.onFailure { e ->
            return Result.failure(e)
        }
    }

    private fun buildAPI(): SuperHeroApi {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(SuperHeroApi::class.java)
    }
}
