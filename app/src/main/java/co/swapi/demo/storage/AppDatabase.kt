package co.swapi.demo.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import co.swapi.demo.model.CharactersCrossRef
import co.swapi.demo.model.Film
import co.swapi.demo.model.People
import co.swapi.demo.storage.dao.FilmDao
import co.swapi.demo.storage.dao.FilmWithCharactersDao
import co.swapi.demo.storage.dao.PeopleDao


@Database(
//    entities = [People::class, Planet::class, Specie::class, Starship::class, Vehicle::class],
    entities = [People::class, Film::class, CharactersCrossRef::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun peopleDao(): PeopleDao
    abstract fun filmDao(): FilmDao
    abstract fun filmWithCharactersDao(): FilmWithCharactersDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                AppDatabase::class.java,"StarWars.db")
                .build()
    }
}
