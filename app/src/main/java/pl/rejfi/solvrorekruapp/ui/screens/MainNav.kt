package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

@Composable
fun MainApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 16.dp
                    )
            ) {
                Button(onClick = {
                    navController.navigate(Destination.SearchScreen)
                }) {
                    Text("Search")
                }
                Button(onClick = {
                    navController.navigate(Destination.FavouriteScreen)
                }) {
                    Text("Favourites")
                }
            }
        },
        content = { ip ->
            NavHost(
                modifier = modifier.padding(ip),
                navController = navController,
                startDestination = Destination.MainScreen
            ) {
                composable<Destination.MainScreen> {
                    MainScreenRoot(
                        viewModel = viewModel,
                        onCocktailClick = {
                            val detailDestination = Destination.DetailScreen(id = it)
                            navController.navigate(detailDestination) {
                                popUpTo(Destination.MainScreen)
                            }
                        })
                }
                composable<Destination.DetailScreen> {
                    val id = it.toRoute<Destination.DetailScreen>().id
                    DetailScreenRoot(
                        viewModel = viewModel,
                        cocktailId = id,
                        onFavouriteClick = { currentFav, favId ->
                            if (currentFav)
                                viewModel.saveFavouriteCocktail(favId)
                            else
                                viewModel.removeFavouriteCocktailId(favId)
                        }
                    )
                }
                composable<Destination.SearchScreen> {
                    SearchScreenRoot(viewModel,
                        onCocktailClick = {
                            val detailDestination = Destination.DetailScreen(id = it)
                            navController.navigate(detailDestination) {
                                popUpTo(Destination.SearchScreen)
                            }
                        })
                }
                composable<Destination.FavouriteScreen> {
                    FavouriteScreenRoot(
                        viewModel = viewModel,
                        modifier = modifier,
                        onCocktailClick = {
                            val detailDestination = Destination.DetailScreen(id = it)
                            navController.navigate(detailDestination) {
                                popUpTo(Destination.FavouriteScreen) {}
                            }
                        })
                }
            }
        })
}