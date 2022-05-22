package com.spider.jetpackcomposedummyproject.note.data.datasource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.spider.jetpackcomposedummyproject.note.domain.model.Note


@Database( version = 1, entities = [Note::class])
abstract class NoteDatabase : RoomDatabase(){

    abstract fun getNoteDao():NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}