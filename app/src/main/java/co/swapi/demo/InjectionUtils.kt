package co.swapi.demo


import android.content.Context
import co.swapi.demo.data.FilmWithCharactersRepository
import co.swapi.demo.data.FilmsRepository
import co.swapi.demo.data.PeopleRepository
import co.swapi.demo.nio.api.StarWarsAPIService
import co.swapi.demo.storage.AppDatabase
import co.swapi.demo.storage.AppLocalCache
import co.swapi.demo.ui.viewmodel.FilmDetailsViewModelFactory
import co.swapi.demo.ui.viewmodel.PeopleDetailsViewModelFactory
import co.swapi.demo.ui.viewmodel.SearchFilmsViewModelFactory
import co.swapi.demo.ui.viewmodel.SearchPeopleViewModelFactory
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object InjectionUtils {

    /**
     * Creates an instance of [AppLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): AppLocalCache {
        return AppLocalCache(AppDatabase.getInstance(context), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [FilmsRepository] based on
     * the [StarWarsAPIService] and a [AppLocalCache]
     */
    private fun provideFilmsRepository(context: Context): FilmsRepository {
        return FilmsRepository.getInstance(StarWarsAPIService.getInstance(), provideCache(context))
    }

    /**
     * Creates an instance of [PeopleRepository] based on
     * the [StarWarsAPIService] and a [AppLocalCache]
     */
    private fun providePeopleRepository(context: Context): PeopleRepository {
        return PeopleRepository.getInstance(StarWarsAPIService.getInstance(), provideCache(context))
    }

    private fun provideFilmWithCharactersRepository(context: Context): FilmWithCharactersRepository {
        return FilmWithCharactersRepository.getInstance(StarWarsAPIService.getInstance(), provideCache(context))
    }

    /**
     * Provides the [SearchFilmsViewModelFactory] that is then used to get a reference to
     * [SearchFilmsViewModel] objects.
     */
    fun provideSearchFilmsViewModelFactory(context: Context): SearchFilmsViewModelFactory {
        return SearchFilmsViewModelFactory(provideFilmsRepository(context))
    }

    fun provideFilmDetailsViewModelFactory(context: Context): FilmDetailsViewModelFactory {
        return FilmDetailsViewModelFactory(provideFilmWithCharactersRepository(context))
    }

    /**
     * Provides the [SearchFilmsViewModelFactory] that is then used to get a reference to
     * [SearchPeopleViewModel] objects.
     */
    fun provideSearchPeopleViewModelFactory(context: Context): SearchPeopleViewModelFactory {
        return SearchPeopleViewModelFactory(providePeopleRepository(context))
    }

    fun providePeopleDetailsViewModelFactory(context: Context): PeopleDetailsViewModelFactory {
        return PeopleDetailsViewModelFactory(providePeopleRepository(context))
    }
}
