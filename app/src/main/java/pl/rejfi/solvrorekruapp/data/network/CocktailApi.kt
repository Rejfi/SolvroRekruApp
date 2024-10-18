package pl.rejfi.solvrorekruapp.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pl.rejfi.solvrorekruapp.data.models.dto.CocktailDto
import pl.rejfi.solvrorekruapp.data.models.dto.CocktailsDto

interface CocktailApi {
    suspend fun getAllCocktails(): Result<CocktailsDto>
    suspend fun getCocktail(id: Int): Result<CocktailDto>
    // suspend fun getCocktailsGlasses()
    // suspend fun getCocktailsCategories()
}

interface IngredientsApi {
    fun getAllIngredients()
    fun getIngredients(id: Int)
    fun getIngredientsTypes()
}

class CocktailNetworkManager : CocktailApi {
    private val baseUrl = "https://cocktails.solvro.pl/api/v1"
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
        }
    }

    override suspend fun getAllCocktails() = runCatching {
        client.get("$baseUrl/cocktails")
            .body<CocktailsDto>()
    }

    override suspend fun getCocktail(id: Int) = runCatching {
        client.get("$baseUrl/cocktail/${id}")
            .body<CocktailDto>()
    }

}