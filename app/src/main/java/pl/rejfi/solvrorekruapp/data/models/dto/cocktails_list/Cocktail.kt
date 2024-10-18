package pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list


import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
/*
 Nie jest to najlepsza praktyka
 Lepiej by bylo miec DTO, Domain etc.
 Aby nie polegac na modelu backendowym w internalowej logice apki
 Zmiana modelu po stronie backendu nie powinna zmieniac warstw wyzej niz przy samym Networkingu
 Tzn. Dto odbiera bezposrednio niezmienioną odp. z serwera
 A Mappery przekładają to na modele domenowe i rozszerzają/skracają o potrzebne pola i\lub zachowania
 Na potrzeby tego PoCa uznajmy, ze moze tak byc
 */
@Entity(tableName = "cocktails_table")
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