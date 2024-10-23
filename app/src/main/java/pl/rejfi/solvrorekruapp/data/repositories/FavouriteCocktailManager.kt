package pl.rejfi.solvrorekruapp.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.network.CocktailApi

class FavouriteCocktailManager(
    private val api: CocktailApi,
    private val datastore: FavouriteCocktailsLocalDatastore
) {
    private var currentPage = 0
    private var lastPage = Int.MAX_VALUE

    @Volatile
    private var end = false

    private val _cocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val cocktails = _cocktails.asStateFlow()

    suspend fun loadNext() {
        val filteredIds = datastore.getFavouriteCocktailIds().firstOrNull() ?: return

        if (filteredIds.isEmpty()) {
            _cocktails.update { emptyList() }
            return
        }

        Log.d("TAG", "Fav: ${filteredIds.toString()}")
        if (end) return

        if (currentPage == lastPage) {
            end = true
        }

        currentPage += 1
        val nextPage = (currentPage).coerceIn(0, lastPage)

        val response = api.getCocktailsByIds(page = nextPage, ids = filteredIds)
        val data = response.getOrNull() ?: return

        val meta = data.meta ?: return
        val cocktails = data.cocktails
        lastPage = meta.lastPage ?: 1

        _cocktails.update { it + cocktails }
        Log.d("TAG", "Next page: $nextPage")
        Log.d("TAG", "Current list: ${_cocktails.value.toString()}")
    }
}