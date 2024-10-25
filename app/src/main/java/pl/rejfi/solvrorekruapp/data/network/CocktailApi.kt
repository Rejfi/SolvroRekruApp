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
import pl.rejfi.solvrorekruapp.data.models.dto.cocktail_categories.CocktailCategory
import pl.rejfi.solvrorekruapp.data.models.dto.cocktail_sort.SortOrder
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktails
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetailsDto

sealed interface SearchValue {
    data object None : SearchValue
    data class Text(
        val text: String,
        val category: CocktailCategory,
        val sort: SortOrder
    ) : SearchValue
}

interface CocktailApi {
    suspend fun getAllCocktails(
        page: Int = 1,
        name: String? = null,
        category: CocktailCategory? = null,
        sort: SortOrder? = null
    ): Result<Cocktails>

    suspend fun getCocktail(id: Int): Result<CocktailDetailsDto>
    suspend fun searchCocktails(searchValue: String): Result<Cocktails>
    suspend fun getCocktailsByIds(page: Int, ids: List<Int>): Result<Cocktails>
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

    override suspend fun getAllCocktails(
        page: Int,
        name: String?,
        category: CocktailCategory?,
        sort: SortOrder?
    ) = runCatching {
        val request = StringBuilder("$baseUrl/cocktails")
            .append("?page=$page")
            .apply {
                if (name != null) append("&name=$name")
            }
            .apply {
                if (category != null) append("&category=${category.value}")
            }
            .apply {
                if (sort != null) append("&sort=${sort.value}")
            }
            .toString()
        client.get(request)
            .body<Cocktails>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

    override suspend fun getCocktail(id: Int) = runCatching {
        client.get("$baseUrl/cocktails/${id}")
            .body<CocktailDetailsDto>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

    override suspend fun searchCocktails(searchValue: String) = runCatching {
        client.get("$baseUrl/cocktails/?name=${searchValue}")
            .body<Cocktails>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

    override suspend fun getCocktailsByIds(page: Int, ids: List<Int>) = runCatching {
        val request = StringBuilder("$baseUrl/cocktails")
            .append("?page=$page")
            .apply {
                if (ids.isNotEmpty()) {
                    ids.forEach {
                        append("&id=$it")
                    }
                }
            }
            .toString()
        client.get(request)
            .body<Cocktails>()
    }.onFailure {
        Log.d("TAG", "${it.message}, ${it.stackTraceToString()}")
    }

}