package co.swapi.demo.model

import androidx.room.Entity

@Entity(primaryKeys = ["filmId", "characterId"])
data class CharactersCrossRef(
    val filmId: Long,
    val characterId: Long
)