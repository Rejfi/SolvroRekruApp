package pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cocktails(
    @SerialName("data")
    val cocktails: List<Cocktail>,
    @SerialName("meta")
    val meta: Meta?
)