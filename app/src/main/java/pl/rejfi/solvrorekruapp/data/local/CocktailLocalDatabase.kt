package pl.rejfi.solvrorekruapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDetailsDomain

@Database(entities = [CocktailDetailsDomain::class], version = 1)
@TypeConverters(Converters::class)
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
                    "fav_cocktails_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}