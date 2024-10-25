package pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail

@Serializable
data class CocktailDetailsDto(
    @SerialName("data")
    val details: Details,
) {
    fun toDomain(): CocktailDetailsDomain {
        return CocktailDetailsDomain(
            alcoholic = details.alcoholic,
            category = details.category,
            createdAt = details.createdAt,
            glass = details.glass,
            id = details.id,
            imageUrl = details.imageUrl,
            ingredients = details.ingredients,
            instructions = details.instructions,
            name = details.name,
            updatedAt = details.updatedAt,
            favourite = false
        )
    }
}

@Entity(tableName = "fav_cocktails")
@Serializable
data class CocktailDetailsDomain(
    @SerialName("alcoholic")
    val alcoholic: Boolean,
    @SerialName("category")
    val category: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("glass")
    val glass: String,
    @SerialName("id")
    @PrimaryKey
    val id: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("ingredients")
    val ingredients: List<Ingredient>,
    @SerialName("instructions")
    val instructions: String,
    @SerialName("name")
    val name: String,
    @SerialName("updatedAt")
    val updatedAt: String,
    @SerialName("favourite")
    val favourite: Boolean = false
) {
    fun toCocktail(): Cocktail {
        return Cocktail(
            alcoholic = this.alcoholic,
            category = this.category,
            createdAt = this.createdAt,
            glass = this.glass,
            id = this.id,
            imageUrl = this.imageUrl,
            instructions = this.instructions,
            name = this.name,
            updatedAt = this.updatedAt
        )
    }
}