package pl.rejfi.solvrorekruapp.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.network.CocktailApi
import pl.rejfi.solvrorekruapp.data.network.SearchValue

class CocktailsNetworkSearcher(private val api: CocktailApi) {
    private var currentPage = 0
    private var lastPage = Int.MAX_VALUE

    private val _cocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktails = _cocktails.asStateFlow()

    @Volatile
    private var end = false

    fun clearResult(){
        _cocktails.update { emptyList() }
    }

    suspend fun loadNext(searchValue: SearchValue = SearchValue.None) {
        if (end) return

        if (currentPage == lastPage) {
            end = true
        }

        currentPage += 1
        val nextPage = (currentPage).coerceIn(0, lastPage)

        val name = when (searchValue) {
            is SearchValue.None -> null
            is SearchValue.Text -> searchValue.text
        }
        val response = api.getAllCocktails(page = nextPage, name = name)
        val data = response.getOrNull() ?: return

        val meta = data.meta ?: return
        val cocktails = data.cocktails
        lastPage = meta.lastPage ?: 1

        if (cocktails.isEmpty()) return

        _cocktails.getAndUpdate { it + cocktails }
        Log.d("TAG", "Next page: $nextPage")
    }
}