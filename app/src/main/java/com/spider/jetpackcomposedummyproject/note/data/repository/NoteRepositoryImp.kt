package com.spider.jetpackcomposedummyproject.note.data.repository

import com.spider.jetpackcomposedummyproject.note.data.datasource.NoteDao
import com.spider.jetpackcomposedummyproject.note.domain.model.Note
import com.spider.jetpackcomposedummyproject.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImp(
        private val dao: NoteDao,
): NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return dao.getNotes()
    }

    override suspend fun getNoteById(id: Int): Note? {
        return dao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note)
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }
}