package pl.rejfi.solvrorekruapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import pl.rejfi.solvrorekruapp.ui.screens.MainApp
import pl.rejfi.solvrorekruapp.ui.theme.SolvroRekruAppTheme
import pl.rejfi.solvrorekruapp.viewmodels.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SolvroRekruAppTheme {
                MainApp(viewModel)
            }
        }
    }
}

