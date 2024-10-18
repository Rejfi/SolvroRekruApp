package pl.rejfi.solvrorekruapp.data.models.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetaDto(
    @SerialName("currentPage")
    val currentPage: Int,
    @SerialName("firstPage")
    val firstPage: Int,
    @SerialName("firstPageUrl")
    val firstPageUrl: String,
    @SerialName("lastPage")
    val lastPage: Int,
    @SerialName("lastPageUrl")
    val lastPageUrl: String,
    @SerialName("nextPageUrl")
    val nextPageUrl: String,
    @SerialName("perPage")
    val perPage: Int,
    @SerialName("previousPageUrl")
    val previousPageUrl: String?,
    @SerialName("total")
    val total: Int
)