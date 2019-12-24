package co.swapi.demo.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class FilmWithCharacters (
    @Embedded val film: Film,
    @Relation(
        parentColumn = "filmId",
        entityColumn = "characterId",
        entity = People::class,
        associateBy = Junction(
            value = CharactersCrossRef::class,
            parentColumn = "filmId",
            entityColumn = "characterId")
    )
    val people: List<People>
)