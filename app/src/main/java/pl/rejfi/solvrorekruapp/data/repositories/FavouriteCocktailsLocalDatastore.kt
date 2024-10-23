package pl.rejfi.solvrorekruapp.data.repositories

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class FavouriteCocktailsLocalDatastore(private val context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "fav_cocktails_prefs")

    suspend fun clearDatastore() {
        context.dataStore.edit {
            it.clear()
        }
    }

    fun getFavouriteCocktailIds(): Flow<List<Int>> {
        Log.d("TAG", "Load fav")
        return context.dataStore.data.map { preferences ->
            val currentValue = preferences[PreferencesKeys.FAV_COCKTAIL_STRING_KEY] ?: ""
            return@map currentValue
                .split(";")
                .filter { it.isNotEmpty() }
                .map { it.toInt() }
        }
    }

    suspend fun saveFavouriteCocktailId(newId: Int) {
        context.dataStore.edit { preferences ->
            Log.d("TAG", "Id: $newId")
            val currentValue = preferences[PreferencesKeys.FAV_COCKTAIL_STRING_KEY] ?: ""
            val idList = currentValue.split(";").filter { it.isNotEmpty() }.toMutableList()

            if (!idList.contains(newId.toString())) {
                Log.d("TAG", "Add Id: $newId")
                idList.add(newId.toString())
                preferences[PreferencesKeys.FAV_COCKTAIL_STRING_KEY] = idList.joinToString(";")
            }
        }
    }

    suspend fun removeFavouriteCocktailId(idToRemove: Int) {
        context.dataStore.edit { preferences ->
            val currentValue = preferences[PreferencesKeys.FAV_COCKTAIL_STRING_KEY] ?: ""
            val idList = currentValue.split(";").filter { it.isNotEmpty() }.toMutableList()

            // Usuwamy identyfikator, jeśli istnieje na liście
            if (idList.contains(idToRemove.toString())) {
                Log.d("TAG", "Remove Id: $idToRemove")
                idList.remove(idToRemove.toString())

                // Zapisujemy zaktualizowaną listę
                preferences[PreferencesKeys.FAV_COCKTAIL_STRING_KEY] = idList.joinToString(";")
            }
        }
    }

    private object PreferencesKeys {
        val FAV_COCKTAIL_STRING_KEY = stringPreferencesKey("favourite_cocktails_key")
    }
}