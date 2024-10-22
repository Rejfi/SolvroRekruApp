package pl.rejfi.solvrorekruapp.ui.screens

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            Button(onClick = {
                navController.navigate(Destination.SearchScreen)
            }) {
                Text("Search")
            }
        }) { ip ->
        NavHost(
            modifier = modifier.windowInsetsPadding(WindowInsets.systemBars),
            navController = navController,
            startDestination = Destination.MainScreen
        ) {
            composable<Destination.MainScreen> {
                MainScreenRoot(
                    viewModel = viewModel,
                    onCocktailClick = {
                        val detailDestination = Destination.DetailScreen(id = it)
                        navController.navigate(detailDestination)

                    })
            }
            composable<Destination.DetailScreen> {
                val id = it.toRoute<Destination.DetailScreen>().id
                DetailScreenRoot(viewModel, id)
            }
            composable<Destination.SearchScreen> {
                SearchScreenRoot(viewModel,
                    onCocktailClick = {
                        val detailDestination = Destination.DetailScreen(id = it)
                        navController.navigate(detailDestination)

                    })
            }
        }
        ip
    }


}