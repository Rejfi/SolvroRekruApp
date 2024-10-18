package pl.rejfi.solvrorekruapp.data.models.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDomain
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailsDomain

@Serializable
data class CocktailsDto(
    @SerialName("data")
    val cocktails: List<CocktailDto>,
    @SerialName("meta")
    val metaDto: MetaDto?
)