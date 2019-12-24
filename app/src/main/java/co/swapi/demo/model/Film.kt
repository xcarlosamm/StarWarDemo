package co.swapi.demo.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@Entity
data class Film(
    @PrimaryKey @field:SerializedName("url") @JsonAdapter(UrlDeserializer::class) var filmId: Long,
    val title: String?,
    @field:SerializedName("episode_id") val episodeId: Int?,
    @field:SerializedName("opening_crawl") var openingCrawl: String?,
    val director: String?,
    val producer: String?,
    @field:SerializedName("release_date") val releaseDate: String?,
    val created: String?,
    val edited: String?
) {
//    @Ignore val cover: Bitmap?
    @Ignore @field:SerializedName("characters") @JsonAdapter(UrlDeserializer::class) val characterIds: List<Long>? = emptyList()

    fun year():String? {
        return releaseDate?.split("-")?.first()
    }
}