package co.swapi.demo.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import co.swapi.demo.model.People

@Dao
interface PeopleDao {
    @Query("SELECT * FROM people")
    fun getAll(): LiveData<List<People>>

    @Query("SELECT * FROM people WHERE characterId IN (:characterIds)")
    fun loadAllByIds(characterIds: List<Long>): LiveData<List<People>>

    @Query("SELECT * FROM people WHERE name LIKE :name")
    fun findByName(name: String): LiveData<List<People>>

    @Query("SELECT * FROM people WHERE characterId = :id")
    fun findById(id: Long): LiveData<People>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(people: List<People>)

    @Delete
    fun delete(character: People)

    @Query("DELETE FROM people")
    fun deleteAll()
}
