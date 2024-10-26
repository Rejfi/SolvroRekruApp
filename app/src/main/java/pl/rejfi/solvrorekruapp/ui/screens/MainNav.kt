package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import pl.rejfi.solvrorekruapp.R
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

@Composable
fun MainApp(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.systemBars),
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
                BottomNavItemIconButton(
                    icon = Icons.Default.Home,
                    contentDescription = stringResource(R.string.nav_home_menu_button),
                    onClick = {
                        navController.navigate(Destination.MainScreen) {
                            popUpTo(Destination.MainScreen) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )

                BottomNavItemIconButton(
                    icon = Icons.Default.Search,
                    contentDescription = stringResource(R.string.nav_search_menu_button),
                    onClick = {
                        navController.navigate(Destination.SearchScreen) {
                            popUpTo(Destination.MainScreen) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
                BottomNavItemIconButton(
                    icon = Icons.Default.Favorite,
                    contentDescription = stringResource(R.string.nav_favourite_menu_button),
                    onClick = {
                        navController.navigate(Destination.FavouriteScreen) {
                            popUpTo(Destination.FavouriteScreen) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                )
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
                        onFavouriteClick = { currentFav, cocktailDetails ->
                            if (currentFav)
                                viewModel.saveFavouriteCocktail(cocktailDetails)
                            else
                                viewModel.removeFavouriteCocktailId(cocktailDetails)
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

@Composable
fun BottomNavItemIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String = "",
    onClick: () -> Unit = {},
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            modifier = Modifier,
            onClick = onClick
        ) {
            Icon(icon, contentDescription)
        }
    }
}