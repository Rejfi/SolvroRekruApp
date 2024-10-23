package pl.rejfi.solvrorekruapp.data.repositories

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import pl.rejfi.solvrorekruapp.data.local.CocktailLocalDatabase
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetails
import pl.rejfi.solvrorekruapp.data.network.CocktailNetworkManager
import pl.rejfi.solvrorekruapp.data.network.SearchValue

class CocktailRepository(app: Application) {
    private val REPO_TAG = "REPO_TAG"
    private val cocktailApi = CocktailNetworkManager()

    private val localCocktailDatabase = CocktailLocalDatabase.getDatabase(app)
    private val cocktailDao = localCocktailDatabase.dataDao()

    private val cocktailsDatastore = FavouriteCocktailsLocalDatastore(app.applicationContext)
    private val cocktailNetworkLoader = CocktailsNetworkLoader(cocktailApi)
    private val cocktailNetworkSearcher = CocktailsNetworkSearcher(cocktailApi)
    private val favouriteCocktailManager = FavouriteCocktailManager(cocktailApi, cocktailsDatastore)

    fun getCocktails(): StateFlow<List<Cocktail>> {
        return cocktailNetworkLoader.cocktails
    }

    fun getFoundCocktails(): StateFlow<List<Cocktail>> {
        return cocktailNetworkSearcher.cocktails
    }

    fun clearFoundCocktails() {
        cocktailNetworkSearcher.clearResult()
    }

    suspend fun loadNextSearchCocktails(searchValue: SearchValue) {
        cocktailNetworkSearcher.loadNext(searchValue)
    }

    suspend fun loadNextCocktails(searchValue: SearchValue) {
        cocktailNetworkLoader.loadNext(searchValue)
    }

    suspend fun loadCocktailsFirstPage() {
        cocktailNetworkLoader.loadCocktailsFirstPage()
    }

    fun getFavouriteCocktails(): StateFlow<List<Cocktail>> {
        return favouriteCocktailManager.cocktails
    }

    suspend fun loadNextFavouriteCocktails() {
        favouriteCocktailManager.loadNext()
    }

    suspend fun saveFavouriteCocktails(id: Int) {
        cocktailsDatastore.saveFavouriteCocktailId(id)
    }

    fun getFavouriteIds(): Flow<List<Int>> {
        return cocktailsDatastore.getFavouriteCocktailIds()
    }

    suspend fun getCocktailById(id: Int): Result<CocktailDetails> {
        return cocktailApi.getCocktail(id)
    }

    suspend fun removeFavouriteCocktailId(id: Int) {
        cocktailsDatastore.removeFavouriteCocktailId(id)
    }
}