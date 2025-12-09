package felix.duan.superherodb.repo

import felix.duan.superherodb.api.ApiService
import felix.duan.superherodb.model.SuperHeroData

interface Repository {
    suspend fun get(id: String): SuperHeroData?

    suspend fun search(name: String): List<SuperHeroData>
}

object SuperHeroRepo : Repository {
    override suspend fun get(id: String): SuperHeroData? {
        LocalRepo.get(id)?.let {
            return it
        }
        val result = ApiService.getById(id).getOrElse { return null }
        return result.also {
            LocalRepo.saveUser(result)
        }
    }

    override suspend fun search(name: String): List<SuperHeroData> {
        LocalRepo.search(name).let {
            if (it.isNotEmpty()) {
                return it
            }
        }

        // TODO: 2025/12/9 (duanyufei) incremental append search result order by id
        val result = ApiService.searchByName(name).getOrElse { return emptyList() }
        return result.results.toList().also { list ->
            list.forEach { LocalRepo.saveUser(it) }
        }
    }
}

// TODO: 2025/12/9 (duanyufei) impl
object LocalRepo : Repository {
    override suspend fun get(id: String): SuperHeroData? = null

    override suspend fun search(name: String): List<SuperHeroData> = emptyList()

    fun saveUser(data: SuperHeroData) {
//        TODO("Not yet implemented")
    }
}