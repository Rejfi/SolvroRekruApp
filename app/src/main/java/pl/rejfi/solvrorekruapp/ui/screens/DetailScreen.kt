package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pl.rejfi.solvrorekruapp.R
import pl.rejfi.solvrorekruapp.data.models.domain.CocktailDetailsDomain
import pl.rejfi.solvrorekruapp.data.models.dto.single_cocktail.Ingredient
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

@Composable
fun DetailScreenRoot(
    viewModel: MainViewModel,
    cocktailId: Int,
    modifier: Modifier = Modifier,
    onFavouriteClick: (Boolean, CocktailDetailsDomain) -> Unit = { _, _ -> }
) {
    LaunchedEffect(Unit) {
        viewModel.selectCocktail(cocktailId)
    }

    val selectedCocktail by viewModel.selectedCocktail.collectAsState()

    if (selectedCocktail == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        DetailScreen(
            cocktail = selectedCocktail!!,
            modifier = modifier,
            onFavouriteClick = onFavouriteClick
        )
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.selectCocktail(null) }
    }
}

@Composable
fun DetailScreen(
    cocktail: CocktailDetailsDomain,
    modifier: Modifier = Modifier,
    onFavouriteClick: (Boolean, CocktailDetailsDomain) -> Unit = { _, _ -> }
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        CocktailDetailsScreen(
            cocktail = cocktail,
            modifier = modifier.padding(innerPadding),
            isFavourite = cocktail.favourite,
            onFavouriteClick = onFavouriteClick
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailDetailsScreen(
    cocktail: CocktailDetailsDomain,
    modifier: Modifier = Modifier,
    isFavourite: Boolean = false,
    onFavouriteClick: (Boolean, CocktailDetailsDomain) -> Unit
) {

    var isCurrentFavourite by remember {
        mutableStateOf(isFavourite)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(cocktail.name)
                        IconButton(
                            onClick = {
                                isCurrentFavourite = !isCurrentFavourite
                                onFavouriteClick(isCurrentFavourite, cocktail)
                            }) {
                            val icon =
                                if (isCurrentFavourite) Icons.Default.Favorite
                                else Icons.Default.FavoriteBorder
                            Icon(icon, "")
                        }
                    }
                },
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            item {
                AsyncImage(
                    model = cocktail.imageUrl,
                    contentDescription = "Cocktail Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_background)
                )
            }

            item {
                Text(
                    text = cocktail.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    if (cocktail.alcoholic) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Alcoholic",
                            tint = Color.Red
                        )
                        Text("Alcoholic", modifier = Modifier.padding(start = 8.dp))
                    } else {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Non-Alcoholic",
                            tint = Color.Green
                        )
                        Text("Non-Alcoholic", modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }

            item {
                Text(
                    text = "Glass: ${cocktail.glass}",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            item {
                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                Text(
                    text = cocktail.instructions,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            items(cocktail.ingredients) { ingredient ->
                IngredientItem(ingredient = ingredient)
            }
        }
    }
}

@Composable
fun IngredientItem(ingredient: Ingredient) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = ingredient.imageUrl,
            contentDescription = ingredient.name,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            placeholder = painterResource(id = R.drawable.ic_launcher_background),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = ingredient.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            ingredient.measure.let {
                Text(text = "Measure: $it", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
