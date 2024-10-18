package pl.rejfi.solvrorekruapp.data.repositories

import android.app.Application
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import pl.rejfi.solvrorekruapp.data.local.CocktailLocalDatabase
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktails
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetails
import pl.rejfi.solvrorekruapp.data.network.CocktailNetworkManager
import kotlin.math.abs

class CocktailRepository(app: Application) {
    private val REPO_TAG = "REPO_TAG"
    private val cocktailApi = CocktailNetworkManager()

    private val localCocktailDatabase = CocktailLocalDatabase.getDatabase(app)
    private val cocktailDao = localCocktailDatabase.dataDao()

    private var lastNetworkUpdate = Long.MIN_VALUE

    suspend fun getAllCocktails(): Result<Cocktails> {
        return cocktailApi.getAllCocktails()
    }

    fun searchCocktail(contains: String): Flow<List<Cocktail>> = flow {
        val cachedCocktails = cocktailDao.searchCocktails(contains).firstOrNull() ?: emptyList()
        emit(cachedCocktails)
    }

    fun getCocktails(): Flow<List<Cocktail>> = flow {
        val cachedCocktails = cocktailDao.getAllCocktails().firstOrNull()

        // Local
        if (cachedCocktails?.isNotEmpty() == true) {
            Log.d(REPO_TAG, "Local cache: ${cachedCocktails.toList().toString()}")
            emit(cachedCocktails)
        }

        debounce {

            val cocktailList = getCocktailsFromNetwork()

            if (cocktailList.isEmpty()) return@flow

            // Update cache
            cocktailDao.insertCocktail(cocktailList)

            Log.d(REPO_TAG, "Network response: ${cocktailList.toList().toString()}")
            emit(cocktailList)
        }
    }

    suspend fun getCocktail(id: Int): Result<CocktailDetails> {
        return cocktailApi.getCocktail(id)
    }

    private inline fun debounce(
        minimumInterval: Long = 60_000L,
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

    private suspend fun getCocktailsFromNetwork(): List<Cocktail> {
        val cocktailsApiResult = cocktailApi.getAllCocktails()

        if (cocktailsApiResult.isFailure) return emptyList()

        val cocktails = cocktailsApiResult.getOrNull() ?: return emptyList()

        val cocktailList = cocktails.cocktails
        return cocktailList
    }

}