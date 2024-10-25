package pl.rejfi.solvrorekruapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.CocktailDetailsDomain

@Dao
interface CocktailDao {

    @Query("SELECT * FROM fav_cocktails WHERE id = :id")
    fun getCocktailById(id: Int): Flow<CocktailDetailsDomain>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCocktail(cocktailList: List<CocktailDetailsDomain>)

    @Delete
    suspend fun deleteCocktail(cocktailList: List<CocktailDetailsDomain>)

    @Query("SELECT * FROM fav_cocktails")
    fun getAllCocktails(): Flow<List<CocktailDetailsDomain>>

    @Query("SELECT * FROM fav_cocktails WHERE name MATCH :contains")
    fun searchCocktails(contains: String): Flow<List<CocktailDetailsDomain>>

    @Query("SELECT EXISTS(SELECT 1 FROM fav_cocktails WHERE id = :id)")
    suspend fun containsCocktailById(id: Int): Int
}
