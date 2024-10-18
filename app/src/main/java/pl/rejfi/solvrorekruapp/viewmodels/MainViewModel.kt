package pl.rejfi.solvrorekruapp.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.rejfi.solvrorekruapp.data.repositories.CocktailRepository

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val repo = CocktailRepository()

    fun getAllCocktails() = viewModelScope.launch(Dispatchers.IO) {
        repo.getAllCocktails()
            .fold(
                onSuccess = {
                    Log.d("TAG", "${it.cocktails.toList().toString()}")
                },
                onFailure = {
                    Log.d("TAG", "${it.stackTraceToString().toString()}")
                })
    }
}