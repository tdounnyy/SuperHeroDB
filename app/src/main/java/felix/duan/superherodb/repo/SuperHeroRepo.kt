package felix.duan.superherodb.repo

import android.content.Context
import androidx.room.Room
import felix.duan.superherodb.api.ApiService
import felix.duan.superherodb.model.SuperHeroData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

interface Repository {
    suspend fun get(id: String): SuperHeroData?

    suspend fun getAll(): List<SuperHeroData>

    suspend fun searchLocal(name: String): List<SuperHeroData>
    
    suspend fun loadMore(page: Int, pageSize: Int, keyword: String? = null): List<SuperHeroData>
}

object SuperHeroRepo : Repository {
    override suspend fun get(id: String): SuperHeroData? {
        return withContext(Dispatchers.IO) {
            LocalRepo.get(id)?.let {
                return@withContext it
            }
            val result = ApiService.getById(id).getOrElse { return@withContext null }
            return@withContext result.also {
                LocalRepo.save(result)
            }
        }
    }

    override suspend fun getAll(): List<SuperHeroData> {
        return withContext(Dispatchers.IO) {
            val local = LocalRepo.getAll()
            if (local.isNotEmpty()) {
                return@withContext local
            }

            val remote = ApiService.getAll()
            return@withContext remote.getOrElse { emptyList() }.also {
                LocalRepo.saveAll(it)
            }
        }
    }

    override suspend fun searchLocal(name: String): List<SuperHeroData> {
        return withContext(Dispatchers.IO) {
            return@withContext LocalRepo.searchLocal(name)
        }
    }

    override suspend fun loadMore(page: Int, pageSize: Int, keyword: String?): List<SuperHeroData> {
        return withContext(Dispatchers.IO) {
            delay(1000) // mocking weak network
            return@withContext LocalRepo.loadMore(page, pageSize, keyword)
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

    override suspend fun getAll(): List<SuperHeroData> {
        return database.superHeroDao().getAll()
    }

    override suspend fun searchLocal(name: String): List<SuperHeroData> {
        return database.superHeroDao().searchByName(name)
    }

    suspend fun save(data: SuperHeroData) {
        database.superHeroDao().insert(data)
    }

    suspend fun saveAll(list: List<SuperHeroData>) {
        database.superHeroDao().insertAll(list)
    }

    suspend fun clearAll() {
        database.superHeroDao().deleteAll()
    }

    override suspend fun loadMore(page: Int, pageSize: Int, keyword: String?): List<SuperHeroData> {
        val allData = if (keyword.isNullOrEmpty()) {
            database.superHeroDao().getAll()
        } else {
            database.superHeroDao().searchByName(keyword)
        }
        
        val start = page * pageSize
        if (start >= allData.size) {
            return emptyList()
        }
        
        val end = minOf(start + pageSize, allData.size)
        return allData.subList(start, end)
    }
}