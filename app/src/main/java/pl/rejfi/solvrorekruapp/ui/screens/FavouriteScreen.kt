package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

@Composable
fun FavouriteScreenRoot(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onCocktailClick: (Int) -> Unit = {},
    onLoadMoreCocktails: () -> Unit = {}
) {

    LaunchedEffect(Unit) {
        viewModel.loadNextFavouriteCocktails()
    }

    val cocktails by viewModel.favouriteCocktails.collectAsStateWithLifecycle()

    FavouriteScreen(
        cocktails = cocktails,
        modifier = modifier,
        onCocktailClick = onCocktailClick,
        onLoadMoreCocktails = { viewModel.loadNextFavouriteCocktails() }
    )
}

@Composable
fun FavouriteScreen(
    cocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    onCocktailClick: (Int) -> Unit = {},
    onLoadMoreCocktails: () -> Unit = {}
) {
    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {},
        content = { innerPadding ->
            CocktailList(
                modifier = Modifier.padding(innerPadding),
                cocktails = cocktails,
                onCocktailClicked = onCocktailClick,
                onLoadMoreCocktails = onLoadMoreCocktails
            )
        })
}