package pl.rejfi.solvrorekruapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail

@Database(entities = [Cocktail::class], version = 1)
abstract class CocktailLocalDatabase : RoomDatabase() {
    abstract fun dataDao(): CocktailDao

    companion object {
        @Volatile
        private var INSTANCE: CocktailLocalDatabase? = null

        fun getDatabase(context: Context): CocktailLocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CocktailLocalDatabase::class.java,
                    "cocktail_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}