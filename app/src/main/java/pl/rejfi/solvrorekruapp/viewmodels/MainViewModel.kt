package pl.rejfi.solvrorekruapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetails
import pl.rejfi.solvrorekruapp.data.repositories.CocktailRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CocktailRepository(app)

    val cocktails: StateFlow<List<Cocktail>> = repo.getCocktails()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _selectedCocktail = MutableStateFlow<CocktailDetails?>(null)
    val selectedCocktail = _selectedCocktail.asStateFlow()

    fun selectCocktail(id: Int?) = viewModelScope.launch(Dispatchers.IO) {
        if (id == null) {
            _selectedCocktail.update { null }
            return@launch
        }

        val response = repo.getCocktail(id)
        if (response.isFailure) return@launch
        val selectedCocktail = response.getOrNull() ?: return@launch

        _selectedCocktail.update { selectedCocktail }
    }

    private val _filteredCocktails = MutableStateFlow<List<Cocktail>>(emptyList())
    val filteredCocktails = _filteredCocktails.asStateFlow()

    fun searchCocktails(s: String) {
        val filteredCocktails = cocktails.value.filter {
            val nameRule = it.name.contains(
                other = s,
                ignoreCase = true
            )
            val instructionRule = it.instructions
                .contains(
                    other = s,
                    ignoreCase = true
                )

            val categoryRule = it.category
                .contains(
                    other = s,
                    ignoreCase = true
                )

            nameRule || instructionRule || categoryRule
        }
        _filteredCocktails.tryEmit(filteredCocktails)
    }
}