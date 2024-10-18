package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil3.compose.AsyncImage
import pl.rejfi.solvrorekruapp.R
import pl.rejfi.solvrorekruapp.data.models.dto.cocktails_list.Cocktail
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel


@Composable
fun MainScreenRoot(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    onCocktailClick: (Int) -> Unit = {}
) {

    var isSearching by remember {
        mutableStateOf(false)
    }

    val finalCocktailList by if (isSearching) viewModel.filteredCocktails.collectAsState()
    else viewModel.cocktails.collectAsState()

    MainContent(
        modifier = modifier,
        cocktails = finalCocktailList,
        onCocktailClick = onCocktailClick,
        onSearchQueryChange = { s ->
            isSearching = s.isNotBlank()
            viewModel.searchCocktails(s)
        })
}

@Composable
fun MainContent(
    cocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    onSearchQueryChange: (String) -> Unit = {},
    onCocktailClick: (Int) -> Unit = {}
) {

    Scaffold(modifier = modifier.fillMaxSize(), topBar = {
        SearchTopBar(
            onQueryChange = onSearchQueryChange
        )
    }, content = { innerPadding ->
        CocktailList(
            modifier = Modifier.padding(innerPadding),
            cocktails = cocktails,
            onCocktailClicked = onCocktailClick
        )
    })
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainContentPreview() {
    val cocktails = listOf(Cocktail(true, "1", "", "", 1, "", "", "Woda ryje banie", ""))
    MainContent(cocktails)
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
                    onQueryChange(query)
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
                keyboardActions = KeyboardActions(onSearch = { })
            )
        },
    )
}

@Composable
fun CocktailList(
    cocktails: List<Cocktail>, modifier: Modifier = Modifier, onCocktailClicked: (Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cocktails) { cocktail ->
            CocktailItem(cocktail = cocktail, onClick = onCocktailClicked)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CocktailListPreview() {
    val cocktails = listOf(Cocktail(true, "1", "", "", 1, "", "", "Woda ryje banie", ""))
    CocktailList(cocktails)
}

@Composable
fun CocktailItem(
    cocktail: Cocktail, onClick: (Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
            .clip(RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { onClick(cocktail.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = cocktail.imageUrl,
            contentDescription = cocktail.name,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = cocktail.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (cocktail.alcoholic) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Alcoholic",
                        tint = Color.Red
                    )
                    Text(text = "Alcoholic", style = MaterialTheme.typography.bodyMedium)
                } else {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Non-Alcoholic",
                        tint = Color.Green
                    )
                    Text(text = "Non-Alcoholic", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}