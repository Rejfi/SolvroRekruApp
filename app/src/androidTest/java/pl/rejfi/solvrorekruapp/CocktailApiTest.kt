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
    fun getCocktailsFirstPage() = runBlocking {
        val response = client.getAllCocktails(1)
        assertEquals(true, response.isSuccess)
        val data = response.getOrThrow()
        assertEquals(true, data.cocktails.isNotEmpty())
    }

    @Test
    fun getAllCocktails() = runBlocking {
        val response = client.getAllCocktails(1)
        assertEquals(true, response.isSuccess)
        val data = response.getOrThrow()
        assertEquals(true, data.cocktails.isNotEmpty())
        val lastPage = data.meta?.lastPage ?: 1

        for (page in 2..lastPage) {
            val res = client.getAllCocktails(page)
            assertEquals(true, res.isSuccess)
            val dat = res.getOrThrow()
            assertEquals(true, dat.cocktails.isNotEmpty())
        }
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

    @Test
    fun searchCocktails() = runBlocking {
        val response = client.getAllCocktails(1, "Mojito")
        assertEquals(true, response.isSuccess)
        val data = response.getOrThrow()
        assertEquals(true, data.cocktails.isNotEmpty())
        val lastPage = data.meta?.lastPage ?: 1

        for (page in 2..lastPage) {
            val res = client.getAllCocktails(page)
            assertEquals(true, res.isSuccess)
            val dat = res.getOrThrow()
            assertEquals(true, dat.cocktails.isNotEmpty())
        }
    }

    @Test
    fun getFavCocktails() = runBlocking {
        val response = client.getCocktailsByIds(1, listOf(11000, 11001, 11002))
        assertEquals(true, response.isSuccess)
        val data = response.getOrThrow()
        assertEquals(true, data.cocktails.isNotEmpty())
        val lastPage = data.meta?.lastPage ?: 1

        for (page in 2..lastPage) {
            val res = client.getAllCocktails(page)
            assertEquals(true, res.isSuccess)
            val dat = res.getOrThrow()
            assertEquals(true, dat.cocktails.isNotEmpty())
        }
    }
}