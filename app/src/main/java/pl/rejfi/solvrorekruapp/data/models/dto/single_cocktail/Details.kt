package pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Details(
    @SerialName("alcoholic")
    val alcoholic: Boolean,
    @SerialName("category")
    val category: String,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("glass")
    val glass: String,
    @SerialName("id")
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
    val updatedAt: String
)