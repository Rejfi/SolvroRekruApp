package pl.rejfi.solvrorekruapp.data.repositories

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDetailsDomain
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetailsDto
import pl.rejfi.solvrorekruapp.data.network.CocktailNetworkManager
import pl.rejfi.solvrorekruapp.data.network.SearchValue

class CocktailRepository(app: Application) {
    private val cocktailApi = CocktailNetworkManager()

    private val favCocktails = FavouriteCocktailsLocalCache(app.applicationContext)
    private val cocktailNetworkLoader = CocktailsNetworkLoader(cocktailApi)
    private val cocktailNetworkSearcher = CocktailsNetworkSearcher(cocktailApi)

    fun getCocktails(): StateFlow<List<Cocktail>> {
        return cocktailNetworkLoader.cocktails
    }

    fun getFoundCocktails(): StateFlow<List<Cocktail>> {
        return cocktailNetworkSearcher.cocktails
    }

    fun clearFoundCocktails() {
        cocktailNetworkSearcher.clearResult()
    }

    suspend fun loadNextSearchCocktails(searchValue: SearchValue, newSearch: Boolean = false) {
        cocktailNetworkSearcher.loadNext(searchValue, newSearch)
    }

    suspend fun loadNextCocktails(searchValue: SearchValue) {
        cocktailNetworkLoader.loadNext(searchValue)
    }

    suspend fun loadCocktailsFirstPage() {
        cocktailNetworkLoader.loadCocktailsFirstPage()
    }

    fun getFavouriteCocktails(): Flow<List<CocktailDetailsDomain>> {
        return favCocktails.getFavouriteCocktails()
    }

    fun getFavouriteCocktailById(id: Int): Flow<CocktailDetailsDomain> {
        return favCocktails.getFavouriteCocktail(id)
    }

    suspend fun saveFavouriteCocktails(cocktailDetailsDomain: CocktailDetailsDomain) {
        favCocktails.saveFavouriteCocktail(cocktailDetailsDomain)
    }

    suspend fun removeFavouriteCocktail(cocktailDetailsDomain: CocktailDetailsDomain) {
        favCocktails.deleteFavouriteCocktail(cocktailDetailsDomain)
    }

    suspend fun isCocktailFavourite(cocktailDetailsDomain: CocktailDetailsDomain): Int {
        return favCocktails.isCocktailFavourite(cocktailDetailsDomain.id)
    }

    suspend fun getCocktailById(id: Int): Result<CocktailDetailsDto> {
        return cocktailApi.getCocktail(id)
    }

}