package pl.rejfi.solvrorekruapp.data.repositories

import android.app.Application
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import pl.rejfi.solvrorekruapp.data.local.CocktailLocalDatabase
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetails
import pl.rejfi.solvrorekruapp.data.network.CocktailNetworkManager
import pl.rejfi.solvrorekruapp.data.network.SearchValue
import kotlin.math.abs

class CocktailRepository(app: Application) {
    private val REPO_TAG = "REPO_TAG"
    private val cocktailApi = CocktailNetworkManager()

    private val localCocktailDatabase = CocktailLocalDatabase.getDatabase(app)
    private val cocktailDao = localCocktailDatabase.dataDao()

    private var lastNetworkUpdate = Long.MIN_VALUE


    private val cocktailNetworkLoader = CocktailsNetworkLoader(cocktailApi)
    private val cocktailNetworkSearcher = CocktailsNetworkSearcher(cocktailApi)

    fun getCocktails(): StateFlow<List<Cocktail>> {
        return cocktailNetworkLoader.cocktails
    }

    fun getFoundCocktails(): StateFlow<List<Cocktail>> {
        return cocktailNetworkSearcher.cocktails
    }

    fun clearFoundCocktails(){
        cocktailNetworkSearcher.clearResult()
    }

    suspend fun loadNextSearchCocktails(searchValue: SearchValue) {
        cocktailNetworkSearcher.loadNext(searchValue)
    }

    suspend fun loadNextCocktails(searchValue: SearchValue) {
        cocktailNetworkLoader.loadNext(searchValue)
    }

    suspend fun getCocktailById(id: Int): Result<CocktailDetails> {
        return cocktailApi.getCocktail(id)
    }

    private inline fun debounce(
        minimumInterval: Long = 200L,
        execute: () -> Unit
    ) {
        val currentTime = System.currentTimeMillis()

        if (abs(currentTime - lastNetworkUpdate) <= minimumInterval) {
            Log.d(REPO_TAG, "Debounce")
            return
        }
        execute()
        lastNetworkUpdate = System.currentTimeMillis()
        Log.d(REPO_TAG, "Not debounce")
    }
}