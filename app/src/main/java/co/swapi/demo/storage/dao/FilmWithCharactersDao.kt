package co.swapi.demo.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.swapi.demo.model.CharactersCrossRef
import co.swapi.demo.model.FilmWithCharacters

@Dao
interface FilmWithCharactersDao {
    @Transaction
    @Query("SELECT * FROM film WHERE filmId = :id")
    fun findById(id: Long): LiveData<FilmWithCharacters>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(filmWithCharacters: List<CharactersCrossRef>)
}
