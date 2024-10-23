package pl.rejfi.solvrorekruapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetails
import pl.rejfi.solvrorekruapp.data.network.SearchValue
import pl.rejfi.solvrorekruapp.data.repositories.CocktailRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CocktailRepository(app)

    val cocktails = repo.getCocktails()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadCocktailsFirstPage() = viewModelScope.launch(Dispatchers.IO) {
        repo.loadCocktailsFirstPage()
    }

    fun loadMoreCocktails(searchValue: SearchValue = SearchValue.None) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.loadNextCocktails(searchValue)
        }

    val foundCocktails: StateFlow<List<Cocktail>> = repo.getFoundCocktails()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun loadNextSearchCocktails(searchValue: SearchValue) = viewModelScope.launch(Dispatchers.IO) {
        repo.loadNextSearchCocktails(searchValue)
    }

    fun clearFoundCocktails() {
        repo.clearFoundCocktails()
    }

    val favouriteCocktails = repo.getFavouriteCocktails()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun loadNextFavouriteCocktails() = viewModelScope.launch(Dispatchers.IO) {
        repo.loadNextFavouriteCocktails()
    }

    fun saveFavouriteCocktail(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.saveFavouriteCocktails(id)
    }

    private val _selectedCocktail = MutableStateFlow<CocktailDetails?>(null)
    val selectedCocktail = _selectedCocktail.asStateFlow()

    fun selectCocktail(id: Int?) = viewModelScope.launch(Dispatchers.IO) {
        if (id == null) {
            _selectedCocktail.update { null }
            return@launch
        }

        val response = repo.getCocktailById(id)

        if (response.isFailure) return@launch
        val selectedCocktail = response.getOrNull() ?: return@launch

        Log.d("TAG", "List: ${selectedCocktail.toString()}")
        val localFavouriteCocktails: List<Int> = repo.getFavouriteIds().first()

        Log.d("TAG", "List: ${localFavouriteCocktails.toString()}")

        val isFavourite = localFavouriteCocktails.contains(selectedCocktail.details.id)
        val cocktail = selectedCocktail.copy(
            favourite = isFavourite
        )
        _selectedCocktail.update { cocktail }
    }

    fun removeFavouriteCocktailId(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repo.removeFavouriteCocktailId(id)
    }
}