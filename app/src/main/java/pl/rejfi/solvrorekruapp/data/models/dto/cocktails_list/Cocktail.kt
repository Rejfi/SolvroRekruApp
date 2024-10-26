package pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list


import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cocktail(
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
    @SerialName("instructions")
    val instructions: String,
    @SerialName("name")
    val name: String,
    @SerialName("updatedAt")
    val updatedAt: String
)