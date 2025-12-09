package felix.duan.superherodb.api

import felix.duan.superherodb.model.ApiResponse
import felix.duan.superherodb.model.SearchResultData
import felix.duan.superherodb.model.SuperHeroData
import retrofit2.http.GET
import retrofit2.http.Path

// TODO: 2025/12/9 (duanyufei) DEBUG ACCESS TOKEN
const val ACCESS_TOKEN = "0eb6582d9082ae358383d679ea23538f"

interface SuperHeroApi {
    @GET("api/${ACCESS_TOKEN}/{id}")
    suspend fun getById(@Path("id") id: String): ApiResponse<SuperHeroData>

    @GET("api/${ACCESS_TOKEN}/search/{name}")
    suspend fun searchByName(@Path("name") name: String): ApiResponse<SearchResultData>
}
