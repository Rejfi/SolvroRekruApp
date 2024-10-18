package pl.rejfi.solvrorekruapp.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Data(
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
    @SerialName("instructions")
    val instructions: String,
    @SerialName("name")
    val name: String,
    @SerialName("updatedAt")
    val updatedAt: String
)