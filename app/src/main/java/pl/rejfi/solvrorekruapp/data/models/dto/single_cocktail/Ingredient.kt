package pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    @SerialName("alcohol")
    val alcohol: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("description")
    val description: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("imageUrl")
    val imageUrl: String?,
    @SerialName("measure")
    val measure: String = "",
    @SerialName("name")
    val name: String,
    @SerialName("percentage")
    val percentage: Int?,
    @SerialName("type")
    val type: String?,
    @SerialName("updatedAt")
    val updatedAt: String
)