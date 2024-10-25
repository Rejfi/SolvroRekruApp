package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.rejfi.solvrorekruapp.R
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

@Composable
fun FavouriteScreenRoot(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onCocktailClick: (Int) -> Unit = {},
) {

    val cocktails by viewModel.favouriteCocktails.collectAsStateWithLifecycle()

    FavouriteScreen(
        cocktails = cocktails.map { it.toCocktail() },
        modifier = modifier,
        onCocktailClick = onCocktailClick,
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
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.favourite_screen_top_welcome_text),
                    fontSize = 22.sp
                )
            }
        },
        content = { innerPadding ->
            CocktailList(
                modifier = Modifier.padding(innerPadding),
                cocktails = cocktails,
                onCocktailClicked = onCocktailClick,
                onLoadMoreCocktails = onLoadMoreCocktails
            )
        })
}