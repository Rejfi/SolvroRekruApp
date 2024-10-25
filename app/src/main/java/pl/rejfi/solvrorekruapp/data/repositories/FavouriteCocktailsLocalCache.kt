package pl.rejfi.solvrorekruapp.data.repositories

import android.content.Context
import kotlinx.coroutines.flow.Flow
import pl.rejfi.solvrorekruapp.data.local.CocktailLocalDatabase
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDetailsDomain

class FavouriteCocktailsLocalCache(context: Context) {
    private val localCocktailDatabase = CocktailLocalDatabase.getDatabase(context)
    private val favCocktailDao = localCocktailDatabase.dataDao()

    suspend fun isCocktailFavourite(id: Int): Int {
        return favCocktailDao.containsCocktailById(id)
    }

    fun getFavouriteCocktails(): Flow<List<CocktailDetailsDomain>> {
        return favCocktailDao.getAllCocktails()
    }

    fun getFavouriteCocktail(id: Int): Flow<CocktailDetailsDomain> {
        return favCocktailDao.getCocktailById(id)
    }

    suspend fun saveFavouriteCocktail(cocktailDetailsDomain: CocktailDetailsDomain) {
        favCocktailDao.insertCocktail(listOf(cocktailDetailsDomain))
    }

    suspend fun deleteFavouriteCocktail(cocktailDetailsDomain: CocktailDetailsDomain) {
        favCocktailDao.deleteCocktail(listOf(cocktailDetailsDomain))
    }
}