package pl.rejfi.solvrorekruapp.data.models.domain


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CocktailsDomain(
    @SerialName("data")
    val cocktails: List<CocktailDomain>,
    @SerialName("meta")
    val metaDomain: MetaDomain
)