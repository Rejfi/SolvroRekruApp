package pl.rejfi.solvrorekruapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail

@Dao
interface CocktailDao {

    @Query("SELECT * FROM cocktails_table WHERE id = :id")
    fun getCocktailDetails(id: Int): Flow<Cocktail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktailList: List<Cocktail>)

    @Delete
    suspend fun deleteCocktail(cocktailList: List<Cocktail>)

    @Query("SELECT * FROM cocktails_table")
    fun getAllCocktails(): Flow<List<Cocktail>>

    @Query("SELECT * FROM cocktails_table WHERE name MATCH :contains")
    fun searchCocktails(contains: String): Flow<List<Cocktail>>
}
