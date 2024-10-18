package pl.rejfi.solvrorekruapp.data.models


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Cocktails(
    @SerialName("data")
    val `data`: List<Data>,
    @SerialName("meta")
    val meta: Meta
)