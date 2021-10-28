package com.spider.jetpackcomposedummyproject.di

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import com.spider.jetpackcomposedummyproject.api.RetrofitApis
import com.spider.jetpackcomposedummyproject.model.PokemonResponseList
import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.ConnectionState
//import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.Constants
import com.spider.jetpackcomposedummyproject.util.currentConnectivityState
import com.spider.jetpackcomposedummyproject.util.observeConnectivityAsFlow
import com.spider.jetpackcomposedummyproject.viewmodel.PokemonViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(api:RetrofitApis) = PokemonRepository(api)

    @Singleton
    @Provides
    fun provideRetrofitApis() : RetrofitApis {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(RetrofitApis::class.java)
    }

    @Singleton
    @Provides
    fun getPokemonListViewModel(@ApplicationContext context:Context,pokemonRepository: PokemonRepository) = PokemonViewModel(
        context,pokemonRepository
    )

}