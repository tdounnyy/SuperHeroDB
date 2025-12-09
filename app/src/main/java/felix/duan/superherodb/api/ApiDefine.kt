package felix.duan.superherodb.api

import felix.duan.superherodb.model.SuperHeroData
import retrofit2.http.GET
import retrofit2.http.Path

interface SuperHeroApi {
    @GET("id/{id}.json")
    suspend fun getById(@Path("id") id: String): SuperHeroData

    @GET("all.json")
    suspend fun getAll(): List<SuperHeroData>
}
