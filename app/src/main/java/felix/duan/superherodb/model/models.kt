package felix.duan.superherodb.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @See [https://akabab.github.io/superhero-api]
 */
@Entity(tableName = "superheroes")
data class SuperHeroData(
    @PrimaryKey
    val id: String,
    val name: String,
    val slug: String,
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
    @Embedded(prefix = "images_")
    val images: Images,
) {
    data class PowerStats(
        val intelligence: Int,
        val strength: Int,
        val speed: Int,
        val durability: Int,
        val power: Int,
        val combat: Int,
    )

    data class Biography(
        val fullName: String,
        val alterEgos: String,
        val aliases: List<String>,
        val placeOfBirth: String,
        val firstAppearance: String,
        val publisher: String?,
        val alignment: String,
    )

    data class Appearance(
        val gender: String,
        val race: String?,
        val height: List<String>,
        val weight: List<String>,
        val eyeColor: String,
        val hairColor: String,
    )

    data class Work(
        val occupation: String,
        val base: String,
    )

    data class Connections(
        val groupAffiliation: String,
        val relatives: String,
    )

    data class Images(
        val xs: String,
        val sm: String,
        val md: String,
        val lg: String,
    )
}
