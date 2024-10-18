package pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CocktailDetails(
    @SerialName("data")
    val details: Details
)