package com.spider.jetpackcomposedummyproject.di

import com.spider.jetpackcomposedummyproject.api.RetrofitApis
import com.spider.jetpackcomposedummyproject.model.PokemonResponseList
import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
//import com.spider.jetpackcomposedummyproject.repository.PokemonRepository
import com.spider.jetpackcomposedummyproject.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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


}