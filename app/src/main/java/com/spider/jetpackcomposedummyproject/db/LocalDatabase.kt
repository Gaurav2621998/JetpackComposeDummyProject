package com.spider.jetpackcomposedummyproject.db

import android.content.Context
import androidx.room.*
import com.spider.jetpackcomposedummyproject.model.Pokemon


@Database(version = 1,entities = [Pokemon::class])
abstract class LocalDatabase:RoomDatabase() {


    abstract fun pokemonDao():PokemonDao

    companion object {
        var localDatabaseInstance: LocalDatabase? = null

        @Synchronized
        fun getInstance(context: Context): LocalDatabase {
            if (localDatabaseInstance == null) {
                synchronized(LocalDatabase::class.java) {
                    localDatabaseInstance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java, "pokemon_db"
                    ).fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return localDatabaseInstance as LocalDatabase
        }
    }
}