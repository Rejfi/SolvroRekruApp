package pl.rejfi.solvrorekruapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import pl.rejfi.solvrorekruapp.data.network.CocktailNetworkManager

@RunWith(AndroidJUnit4::class)
class CocktailApiTest {

    private val client = CocktailNetworkManager()

    @Test
    fun getAllCocktails() = runBlocking {
        val response = client.getAllCocktails()
        assertEquals(true, response.isSuccess)
        val data = response.getOrThrow()
        assertEquals(true, data.cocktails.isNotEmpty())
    }

    @Test
    fun getSingleCocktail() = runBlocking {
        val response = client.getAllCocktails()
        assertEquals(true, response.isSuccess)
        val data = response.getOrThrow().cocktails.first()
        val singleCocktailResponse = client.getCocktail(data.id)
        assertEquals(true, singleCocktailResponse.isSuccess)
        val cocktail = singleCocktailResponse.getOrThrow()
        val id = cocktail.details.id
        assertEquals(data.id, id)
    }
}