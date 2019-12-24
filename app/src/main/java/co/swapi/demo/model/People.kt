package co.swapi.demo.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName

@Entity
data class People(
    @PrimaryKey @field:SerializedName("url") @JsonAdapter(UrlDeserializer::class) var characterId: Long,
    val name: String?,
    @field:SerializedName("birth_year") val birthYear: String?,
    @field:SerializedName("eye_color") val eyeColor: String?,
    val gender: String?,
    @field:SerializedName("hair_color") val hairColor: String?,
    val height: String?,
    val mass: String?,
    @field:SerializedName("skin_color") val skinColor: String?,
    val homeworld: String?,
    val created: String?,
    val edited: String?
) {
//    @Ignore val cover: Bitmap?
}

