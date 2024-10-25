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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDetailsDomain
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

    fun loadNextSearchCocktails(
        searchValue: SearchValue,
        newSearch: Boolean = false
    ) = viewModelScope.launch(Dispatchers.IO) {
        repo.loadNextSearchCocktails(searchValue, newSearch)
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

    fun saveFavouriteCocktail(cocktailDetailsDomain: CocktailDetailsDomain) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveFavouriteCocktails(cocktailDetailsDomain)
        }

    private val _selectedCocktail = MutableStateFlow<CocktailDetailsDomain?>(null)
    val selectedCocktail = _selectedCocktail.asStateFlow()

    fun selectCocktail(id: Int?) = viewModelScope.launch(Dispatchers.IO) {
        if (id == null) {
            _selectedCocktail.update { null }
            return@launch
        }

        val cachedFavCocktail = repo.getFavouriteCocktailById(id).firstOrNull()
        if (cachedFavCocktail != null) {
            _selectedCocktail.update { cachedFavCocktail.copy(favourite = true) }
            Log.d("TAG", "Restore from cache")
            return@launch
        }

        val response = repo.getCocktailById(id)

        if (response.isFailure) return@launch
        val selectedCocktailDto = response.getOrNull() ?: return@launch
        val selectedCocktailDomain = selectedCocktailDto.toDomain()

        val isFavourite = repo.isCocktailFavourite(selectedCocktailDomain)
        val cocktail = selectedCocktailDomain.copy(
            favourite = isFavourite == 1
        )
        _selectedCocktail.update { cocktail }
    }

    fun removeFavouriteCocktailId(cocktailDetailsDomain: CocktailDetailsDomain) =
        viewModelScope.launch(Dispatchers.IO) {
            repo.removeFavouriteCocktail(cocktailDetailsDomain)
        }
}