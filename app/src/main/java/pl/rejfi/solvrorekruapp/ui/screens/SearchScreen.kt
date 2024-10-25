package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.rejfi.solvrorekruapp.R
import pl.rejfi.solvrorekruapp.data.models.dto.cocktail_categories.CocktailCategory
import pl.rejfi.solvrorekruapp.data.models.dto.cocktail_sort.SortOrder
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
    var selectedCategory by remember {
        mutableStateOf(CocktailCategory.ALL)
    }
    var query by remember {
        mutableStateOf("")
    }

    var order by remember {
        mutableStateOf(SortOrder.NAME_ASC)
    }

    SearchScreen(
        foundCocktails = cocktails,
        onSearch = { s ->
            query = s
            val searchValue = SearchValue.Text(query, selectedCategory, order)
            viewModel.loadNextSearchCocktails(searchValue, true)
        },
        onCocktailClick = onCocktailClick,
        onCategorySelected = { selectedCategory = it },
        onSortOrderSelected = { order = it },
        onLoadMoreCocktails = {
            val searchValue = SearchValue.Text(query, selectedCategory, order)
            viewModel.loadNextSearchCocktails(searchValue, false)
        }
    )

    DisposableEffect(Unit) {
        onDispose { viewModel.clearFoundCocktails() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    foundCocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    onCocktailClick: (Int) -> Unit = {},
    onLoadMoreCocktails: () -> Unit = {},
    onCategorySelected: (CocktailCategory) -> Unit = {},
    onSortOrderSelected: (SortOrder) -> Unit = {}
) {
    var selectedCategory by rememberSaveable { mutableStateOf(CocktailCategory.ALL) }
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = stringResource(
                            R.string.search_screen_top_welcome_text
                        ), fontSize = 22.sp
                    )
                }

                SearchTopBar(onQueryChange = { query = it })

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    TextField(
                        readOnly = true,
                        value = selectedCategory.value,
                        onValueChange = {},
                        label = { Text("Wybierz kategorię") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 8.dp)
                            .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                            .clickable { expanded = !expanded }
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        CocktailCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.value) },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                    onCategorySelected(category)
                                }
                            )
                        }
                    }
                }

                SortDropdownMenu(
                    onSortOrderSelected = onSortOrderSelected
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onSearch(query) }) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        content = { innerPadding ->
            CocktailList(
                modifier = Modifier.padding(innerPadding),
                cocktails = foundCocktails,
                onCocktailClicked = onCocktailClick,
                onLoadMoreCocktails = onLoadMoreCocktails
            )
        }
    )
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
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SortDropdownMenu(
    onSortOrderSelected: (SortOrder) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var order by remember {
        mutableStateOf(SortOrder.NAME_ASC)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            readOnly = true,
            value = order.value,
            onValueChange = {},
            label = { Text("Wybierz sortowanie") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 8.dp)
                .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                .clickable { expanded = !expanded }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            SortOrder.entries.forEach { sortOrder ->
                DropdownMenuItem(
                    text = { Text(sortOrder.value) },
                    onClick = {
                        order = sortOrder
                        onSortOrderSelected(order)
                        expanded = false
                    }
                )
            }
        }
    }
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