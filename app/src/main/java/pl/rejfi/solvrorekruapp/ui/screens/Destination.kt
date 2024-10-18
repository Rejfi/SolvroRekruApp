package pl.rejfi.solvrorekruapp.ui.screens

import kotlinx.serialization.Serializable

sealed interface Destination {

    @Serializable
    data object MainScreen : Destination

    @Serializable
    data class DetailScreen(val id: Int) : Destination
}