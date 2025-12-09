package felix.duan.superherodb.model

import com.google.gson.annotations.SerializedName

/**
 * @See [https://superheroapi.com]
 */

data class SuperHeroData(
    val response: String,
    val error: String?,
    val id: String,
    val name: String,
    val powerStats: PowerStats,
    val biography: Biography,
    val appearance: Appearance,
    val work: Work,
    val connections: Connections,
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
    val response: String,
    val error: String?,
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

// TODO: 2025/12/9 (duanyufei) impl custom Parser
// SuperHeroData & SearchResultData
data class SuperHeroResponse<T>(
    val response: String,
    val error: String?,
    val data: T,
)
