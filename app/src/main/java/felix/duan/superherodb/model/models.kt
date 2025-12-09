package felix.duan.superherodb.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType

/**
 * @See [https://superheroapi.com]
 */

@Entity(tableName = "superheroes")
data class SuperHeroData(
    @PrimaryKey
    val id: String,
    val name: String,
    @Embedded(prefix = "powerstats_")
    @SerializedName("powerstats")
    val powerStats: PowerStats,
    @Embedded(prefix = "biography_")
    val biography: Biography,
    @Embedded(prefix = "appearance_")
    val appearance: Appearance,
    @Embedded(prefix = "work_")
    val work: Work,
    @Embedded(prefix = "connections_")
    val connections: Connections,
    @Embedded(prefix = "image_")
    val image: Image,
) {
    data class PowerStats(
        val intelligence: String,
        val strength: String,
        val speed: String,
        val durability: String,
        val power: String,
        val combat: String,
    )

    data class Biography(
        @SerializedName("full-name")
        val fullName: String,
        @SerializedName("alter-egos")
        val alterEgos: String,
        val aliases: List<String>,
        @SerializedName("place-of-birth")
        val placeOfBirth: String,
        @SerializedName("first-appearance")
        val firstAppearance: String,
        val publisher: String,
        val alignment: String,
    )

    data class Appearance(
        val gender: String,
        val race: String,
        val height: List<String>,
        val weight: List<String>,
        @SerializedName("eye-color")
        val eyeColor: String,
        @SerializedName("hair-color")
        val hairColor: String,
    )

    data class Work(
        val occupation: String,
        val base: String,
    )

    data class Connections(
        @SerializedName("group-affiliation")
        val groupAffiliation: String,
        val relatives: String,
    )

    data class Image(
        val url: String,
    )
}

data class SearchResultData(
    val resultsFor: String,
    val results: Array<SuperHeroData>,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchResultData

        if (resultsFor != other.resultsFor) return false
        if (!results.contentEquals(other.results)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resultsFor.hashCode()
        result = 31 * result + results.contentHashCode()
        return result
    }
}

data class ApiResponse<T>(
    val success: Boolean,
    val errorMsg: String?,
    val data: T?,
)

class ApiResponseTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

        if (!ApiResponse::class.java.isAssignableFrom(type.rawType)) {
            return null
        }

        val delegateAdapter = gson.getDelegateAdapter(this, type)
        val jsonElementAdapter = gson.getAdapter(JsonElement::class.java)

        return object : TypeAdapter<T>() {
            override fun write(out: JsonWriter, value: T) {
                delegateAdapter.write(out, value)
            }

            override fun read(input: JsonReader): T {
                val jsonElement = jsonElementAdapter.read(input)

                val jsonObject = jsonElement.asJsonObject
                val response = jsonObject.get("response")?.asString.orEmpty() // status
                val error = jsonObject.get("error")?.asString.orEmpty() // error msg

                return if (response == "success") {
                    // 拿到泛型 T 的实际类型（例如 ApiResponse<User> 中的 User）
                    val actualType = (type.type as ParameterizedType).actualTypeArguments[0]
                    val data = gson.fromJson<Any>(jsonObject, actualType)

                    ApiResponse(true, null, data) as T
                } else {
                    ApiResponse<Any>(false, error, null) as T
                }
            }
        } as TypeAdapter<T>
    }
}
