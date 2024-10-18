package pl.rejfi.solvrorekruapp.data.repositories

import pl.rejfi.solvrorekruapp.data.models.dto.CocktailsDto
import pl.rejfi.solvrorekruapp.data.network.CocktailNetworkManager

class CocktailRepository {
    private val cocktailApi = CocktailNetworkManager()

    suspend fun getAllCocktails(): Result<CocktailsDto> {
        return cocktailApi.getAllCocktails()
    }
}