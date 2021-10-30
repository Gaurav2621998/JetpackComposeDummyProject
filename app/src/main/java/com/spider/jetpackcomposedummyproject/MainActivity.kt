package com.spider.jetpackcomposedummyproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import androidx.paging.ExperimentalPagingApi
import com.spider.jetpackcomposedummyproject.ui.screens.PokemonDetailScreen
import com.spider.jetpackcomposedummyproject.ui.screens.PokemonListScreen
import com.spider.jetpackcomposedummyproject.ui.theme.JetpackComposeDummyProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    @ExperimentalPagingApi
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            setContent {
                JetpackComposeDummyProjectTheme {
                    MyApp()
                }
            }
        }
        catch (e:Exception){
            Timber.d("exception "+e.message)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagingApi
@ExperimentalFoundationApi
@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "pokemon_list_screen"
    ) {

        composable("pokemon_list_screen") {
                // showing list of pokemon
                PokemonListScreen(navController = navController)
        }

        composable(
            "pokemon_detail_screen/{dominantColor}/{pokemonName}",
            arguments = listOf(
                navArgument("dominantColor") {
                    type = NavType.IntType
                },
                navArgument("pokemonName") {
                    type = NavType.StringType
                }
            )
        ) {
            val dominantColor = remember {
                 val color = it.arguments?.getInt("dominantColor")
                 color?.let { Color(it) } ?: Color.White
            }
            val pokemonName = remember {
                it.arguments?.getString("pokemonName")
            }

            PokemonDetailScreen(
                dominantColor = dominantColor,
                pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                navController = navController)
        }
    }
}

@ExperimentalAnimationApi
@ExperimentalPagingApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeDummyProjectTheme {
        MyApp()
    }
}