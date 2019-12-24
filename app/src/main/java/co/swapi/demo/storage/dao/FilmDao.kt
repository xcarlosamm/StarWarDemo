package co.swapi.demo.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.swapi.demo.model.Film

@Dao
interface FilmDao {
    @Query("SELECT * FROM film")
    fun getAll(): LiveData<List<Film>>

    @Query("SELECT * FROM film WHERE filmId IN (:filmIds)")
    fun loadAllByIds(filmIds: List<Long>): LiveData<List<Film>>

    @Query("SELECT * FROM film WHERE title LIKE :title")
    fun findByName(title: String): LiveData<List<Film>>

    @Query("SELECT * FROM film WHERE filmId = :id")
    fun findById(id: Long): LiveData<Film>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(films: List<Film>)

    @Delete
    fun delete(film: Film)

    @Query("DELETE FROM film")
    fun deleteAll()
}
