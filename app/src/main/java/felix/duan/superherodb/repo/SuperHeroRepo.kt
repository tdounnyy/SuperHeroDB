package felix.duan.superherodb.repo

import android.content.Context
import androidx.room.Room
import felix.duan.superherodb.api.ApiService
import felix.duan.superherodb.model.SuperHeroData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList

interface Repository {
    suspend fun get(id: String): SuperHeroData?

    suspend fun getAllLocally(): List<SuperHeroData>

    suspend fun search(name: String): List<SuperHeroData>
}

object SuperHeroRepo : Repository {
    override suspend fun get(id: String): SuperHeroData? {
        return withContext(Dispatchers.IO) {
            LocalRepo.get(id)?.let {
                return@withContext it
            }
            val result = ApiService.getById(id).getOrElse { return@withContext null }
            return@withContext result.also {
                LocalRepo.saveUser(result)
            }
        }
    }

    override suspend fun getAllLocally(): List<SuperHeroData> {
        return withContext(Dispatchers.IO) {
            LocalRepo.getAllLocally()
        }
    }

    override suspend fun search(name: String): List<SuperHeroData> {
        return withContext(Dispatchers.IO) {
            LocalRepo.search(name).let {
                if (it.isNotEmpty()) {
                    return@withContext it
                }
            }

            // TODO: 2025/12/9 (duanyufei) incremental append search result order by id
            val result = ApiService.searchByName(name).getOrElse { return@withContext emptyList() }
            return@withContext result.results.toList().also {
                LocalRepo.saveAllUsers(it)
            }
        }
    }
}

object LocalRepo : Repository {
    private lateinit var database: SuperHeroDatabase

    fun init(context: Context) {
        database = Room.databaseBuilder(
            context.applicationContext,
            SuperHeroDatabase::class.java,
            "superhero-db"
        ).build()
    }

    override suspend fun get(id: String): SuperHeroData? {
        return database.superHeroDao().getById(id)
    }

    override suspend fun getAllLocally(): List<SuperHeroData> {
        return database.superHeroDao().getAllLocally()
    }

    override suspend fun search(name: String): List<SuperHeroData> {
        return database.superHeroDao().searchByName(name)
    }

    suspend fun saveUser(data: SuperHeroData) {
        database.superHeroDao().insert(data)
    }

    suspend fun saveAllUsers(users: List<SuperHeroData>) {
        database.superHeroDao().insertAll(users)
    }

    suspend fun clearAll() {
        database.superHeroDao().deleteAll()
    }
}