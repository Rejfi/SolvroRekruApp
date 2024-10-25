package pl.rejfi.solvrorekruapp.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pl.rejfi.solvrorekruapp.data.models.dto.cocktail_categories.CocktailCategory
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.network.CocktailApi
import pl.rejfi.solvrorekruapp.data.network.SearchValue
import pl.rejfi.solvrorekruapp.plusUnique

class CocktailsNetworkSearcher(private val api: CocktailApi) {
    private var currentPage = 0
    private var lastPage = Int.MAX_VALUE

    private val _cocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktails = _cocktails.asStateFlow()

    private val loadingMutex = Mutex()

    fun clearResult() {
        _cocktails.update { emptyList() }
        currentPage = 0
        lastPage = Int.MAX_VALUE
    }

    suspend fun loadNext(
        searchValue: SearchValue = SearchValue.None,
        newSearch: Boolean = false
    ) {
        loadingMutex.withLock {
            if (newSearch) {
                clearResult()
            }

            if (currentPage > lastPage) return
            currentPage += 1
            val nextPage = currentPage.coerceIn(0, lastPage)

            val (name, category, order) = when (searchValue) {
                is SearchValue.None -> Triple(null, null, null)
                is SearchValue.Text -> Triple(
                    searchValue.text,
                    searchValue.category,
                    searchValue.sort
                )
            }
            val response = api.getAllCocktails(
                page = nextPage,
                name = name,
                category = if (category == CocktailCategory.ALL) null else category,
                sort = order
            )
            val data = response.getOrNull() ?: return

            val meta = data.meta ?: return
            val cocktails = data.cocktails
            lastPage = meta.lastPage ?: 1

            if (cocktails.isEmpty()) return

            _cocktails.getAndUpdate { it.plusUnique(cocktails) }
            Log.d("TAG", "Next page: $nextPage")
        }
    }
}