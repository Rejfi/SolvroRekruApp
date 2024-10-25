package pl.rejfi.solvrorekruapp.data.repositories

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.network.CocktailApi
import pl.rejfi.solvrorekruapp.data.network.SearchValue

class CocktailsNetworkLoader(private val api: CocktailApi) {
    private var currentPage = 0
    private var lastPage = Int.MAX_VALUE

    @Volatile
    private var end = false

    private val _cocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktails = _cocktails.asStateFlow()

    private var init = false
    private val loadingMutex = Mutex()

    suspend fun loadCocktailsFirstPage() {
        if (init) return

        init = true
        currentPage = 1
        loadPage(currentPage)
    }

    suspend fun loadNext(searchValue: SearchValue = SearchValue.None) {
        if (end) return
        if (currentPage == lastPage) {
            end = true
            return
        }

        currentPage += 1
        loadPage(currentPage, searchValue)
    }

    private suspend fun loadPage(page: Int, searchValue: SearchValue = SearchValue.None) {
        loadingMutex.withLock {
            val name = when (searchValue) {
                is SearchValue.None -> null
                is SearchValue.Text -> searchValue.text
            }
            val response = api.getAllCocktails(page = page, name = name)
            if (response.isFailure) {
                init = false
                end = false
            }
            val data = response.getOrNull() ?: return

            val meta = data.meta ?: return
            val cocktails = data.cocktails
            lastPage = meta.lastPage ?: 1

            if (cocktails.isEmpty()) return

            _cocktails.getAndUpdate { it + cocktails }
            Log.d("TAG", "Page loaded: $page")
        }
    }
}