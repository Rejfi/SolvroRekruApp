package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.data.network.SearchValue
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

@Composable
fun SearchScreenRoot(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onCocktailClick: (Int) -> Unit = {}
) {
    val cocktails by viewModel.foundCocktails.collectAsStateWithLifecycle()

    SearchScreen(
        foundCocktails = cocktails,
        onSearch = { s ->
            val searchValue = SearchValue.Text(s)
            viewModel.loadNextSearchCocktails(searchValue)
        },
        onCocktailClick = onCocktailClick
    )

    DisposableEffect(Unit) {
        onDispose { viewModel.clearFoundCocktails() }
    }
}

@Composable
fun SearchScreen(
    foundCocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    onCocktailClick: (Int) -> Unit = {},
    onLoadMoreCocktails: () -> Unit = {}
) {

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            SearchTopBar(
                onQueryChange = onSearch
            )
        },
        content = { innerPadding ->
            CocktailList(
                modifier = Modifier.padding(innerPadding),
                cocktails = foundCocktails,
                onCocktailClicked = onCocktailClick,
                onLoadMoreCocktails = onLoadMoreCocktails
            )
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onQueryChange: (String) -> Unit = {},
) {

    var query by rememberSaveable {
        mutableStateOf("")
    }

    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = {
                    query = it
                    // onQueryChange(query)
                },
                placeholder = { Text("Szukaj...") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                colors = TextFieldDefaults.colors(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Szukaj"
                    )
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {

                        IconButton(onClick = {
                            query = ""
                            onQueryChange(query)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Clear, contentDescription = "Wyczyść"
                            )
                        }
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(onSearch = { onQueryChange(query) })
            )
        },
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SearchScreenPreview(modifier: Modifier = Modifier) {
    val cocktails = listOf(
        Cocktail(
            alcoholic = true,
            category = "Cocktail",
            createdAt = "2023-09-15",
            glass = "Highball glass",
            id = 1,
            imageUrl = "https://example.com/cocktail1.jpg",
            instructions = "Mix all ingredients and serve over ice.",
            name = "Mojito",
            updatedAt = "2024-10-01"
        ),
        Cocktail(
            alcoholic = false,
            category = "Mocktail",
            createdAt = "2023-08-10",
            glass = "Collins glass",
            id = 2,
            imageUrl = "https://example.com/cocktail2.jpg",
            instructions = "Combine ingredients in a shaker, shake well, and serve chilled.",
            name = "Virgin Piña Colada",
            updatedAt = "2024-09-30"
        ),
        Cocktail(
            alcoholic = true,
            category = "Shot",
            createdAt = "2023-07-25",
            glass = "Shot glass",
            id = 3,
            imageUrl = "https://example.com/cocktail3.jpg",
            instructions = "Layer the ingredients in a shot glass and serve immediately.",
            name = "B52",
            updatedAt = "2024-08-25"
        )
    )

    SearchScreen(cocktails)
}