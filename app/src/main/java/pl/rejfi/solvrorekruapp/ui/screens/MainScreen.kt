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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

    LaunchedEffect(Unit) {
        viewModel.loadCocktailsFirstPage()
    }

    val cocktails by viewModel.cocktails.collectAsStateWithLifecycle()

    MainScreen(
        modifier = modifier,
        cocktails = cocktails,
        onCocktailClick = onCocktailClick,
        onLoadMoreCocktails = {
            viewModel.loadMoreCocktails()
        })
}

@Composable
fun MainScreen(
    cocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    onCocktailClick: (Int) -> Unit = {},
    onLoadMoreCocktails: () -> Unit = {}
) {

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.main_screen_top_welcome_text),
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

@Composable
fun CocktailList(
    cocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    onCocktailClicked: (Int) -> Unit = {},
    onLoadMoreCocktails: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        itemsIndexed(cocktails) { index, cocktail ->
            CocktailItem(cocktail = cocktail, onClick = onCocktailClicked)

            if (index >= cocktails.size - 3 && cocktails.size > 10) {
                onLoadMoreCocktails()
            }
        }
    }
}

@Composable
fun CocktailItem(
    cocktail: Cocktail,
    onClick: (Int) -> Unit = {},
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