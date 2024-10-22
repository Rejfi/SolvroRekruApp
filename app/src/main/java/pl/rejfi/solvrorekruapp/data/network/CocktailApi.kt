package pl.rejfi.solvrorekruapp.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktails
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetails
import pl.rejfi.solvrorekruapp.ui.screens.Destination

sealed interface SearchValue {
    data object None : SearchValue
    data class Text(val text: String) : SearchValue
}

interface CocktailApi {
    suspend fun getAllCocktails(
        page: Int = 1,
        name: String? = null
    ): Result<Cocktails>

    suspend fun getCocktail(id: Int): Result<CocktailDetails>
    suspend fun searchCocktails(searchValue: String): Result<Cocktails>
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
            logger = Logger.ANDROID
        }
    }

    override suspend fun getAllCocktails(page: Int, name: String?) = runCatching {
        val request = StringBuilder("$baseUrl/cocktails")
            .append("?page=$page")
            .apply {
                if (name != null) append("&name=$name")
            }
            .toString()
        client.get(request)
            .body<Cocktails>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

    override suspend fun getCocktail(id: Int) = runCatching {
        client.get("$baseUrl/cocktails/${id}")
            .body<CocktailDetails>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

    override suspend fun searchCocktails(searchValue: String) = runCatching {
        client.get("$baseUrl/cocktails/?name=${searchValue}")
            .body<Cocktails>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

}