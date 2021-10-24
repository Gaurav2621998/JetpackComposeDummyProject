package com.spider.jetpackcomposedummyproject.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.transform.GrayscaleTransformation
import com.spider.jetpackcomposedummyproject.R
import com.spider.jetpackcomposedummyproject.model.Pokemon
import com.spider.jetpackcomposedummyproject.ui.theme.RobotoCondensed
import com.spider.jetpackcomposedummyproject.viewmodel.PokemonViewModel
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun PokemonListScreen(
    navController: NavController
) {
    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription = "Pokemon",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally)
            )
            SearchBar(
                hint = "Search...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {

            }
            Spacer(modifier = Modifier.height(16.dp))
            PokemonList(navController = navController)
        }
    }
}


// TODO Not working yet
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    // hide hint when Textfield active
                    isHintDisplayed = !it.isFocused
                }
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val endReached by remember { viewModel.endReached }
    val loadError by remember { viewModel.loadError }
    val isLoading by remember { viewModel.isLoading }

    // using grid view
    LazyVerticalGrid(
        cells =
        GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(pokemonList.size) {
            if (!endReached) {
                viewModel.loadPokemonPaginated()
            }
            PokemonCard(entry = pokemonList[it], navController = navController)
        }
    }



    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // showing progress bar when data loading
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        }

        // showing retry option when error occur
        if (loadError.isNotEmpty()) {
            RetrySection(error = loadError) {
                viewModel.loadPokemonPaginated()
            }
        }
    }

}

@Composable
fun PokemonCard(
    entry: Pokemon,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonViewModel = hiltViewModel()
) {
    val defaultDominantColor = MaterialTheme.colors.surface
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    Box(modifier = Modifier.padding(16.dp)) {
        Box(
            contentAlignment = Center,
            modifier = modifier
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .aspectRatio(1f)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            dominantColor,
                            defaultDominantColor
                        )
                    )
                )
                .clickable {
                    navController.navigate(
                        "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                    )
                }
        ) {
            Column {
                val imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(entry.imageUrl)
                    .build()

                val imageLoader = ImageLoader(LocalContext.current)
                val imagePainter = rememberImagePainter(
                    request = imageRequest,
                    imageLoader = imageLoader
                )

                LaunchedEffect(key1 = imagePainter) {
                    launch {
                        val result = imageLoader.execute(imageRequest)
                        if(result is SuccessResult) {
                            viewModel.calcDominantColor(result.drawable) { color ->
                                dominantColor = color
                            }
                        }
                    }
                }

                val painter =
                    rememberImagePainter(data =entry.imageUrl)
                Image(
                    painter = painter,
                    contentDescription = entry.pokemonName,
                    modifier = Modifier
                        .size(120.dp)
                        .align(CenterHorizontally)
                )

                Text(
                    text = entry.pokemonName,
                    fontFamily = RobotoCondensed,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(error, color = Color.Red, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onRetry() },
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}