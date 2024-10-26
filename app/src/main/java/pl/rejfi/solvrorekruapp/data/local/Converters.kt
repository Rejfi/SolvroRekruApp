package pl.rejfi.solvrorekruapp.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.Ingredient

class Converters {
    @TypeConverter
    fun fromIngredientList(ingredients: List<Ingredient>): String {
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun toIngredientList(ingredientsString: String): List<Ingredient> {
        return Json.decodeFromString(ingredientsString)
    }
}
