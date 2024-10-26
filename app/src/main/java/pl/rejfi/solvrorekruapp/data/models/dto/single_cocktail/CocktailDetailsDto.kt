package pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDetailsDomain

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

