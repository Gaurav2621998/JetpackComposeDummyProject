package com.spider.jetpackcomposedummyproject.note.domain.repository

import com.spider.jetpackcomposedummyproject.note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun getNoteById(id:Int):Note?

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)

}