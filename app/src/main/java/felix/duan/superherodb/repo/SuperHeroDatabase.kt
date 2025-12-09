package felix.duan.superherodb.repo

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import felix.duan.superherodb.model.SuperHeroData

@Dao
interface SuperHeroDao {
    @Query("SELECT * FROM superheroes WHERE id = :id")
    suspend fun getById(id: String): SuperHeroData?

    @Query("SELECT * FROM superheroes WHERE name LIKE '%' || :name || '%'")
    suspend fun searchByName(name: String): List<SuperHeroData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(superHero: SuperHeroData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(superHeroes: List<SuperHeroData>)

    @Delete
    suspend fun delete(superHero: SuperHeroData)

    @Query("DELETE FROM superheroes")
    suspend fun deleteAll()
}

@Database(
    entities = [SuperHeroData::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class SuperHeroDatabase : RoomDatabase() {
    abstract fun superHeroDao(): SuperHeroDao
}

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}