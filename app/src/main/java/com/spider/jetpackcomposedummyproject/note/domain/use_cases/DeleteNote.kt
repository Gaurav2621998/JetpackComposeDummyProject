package com.spider.jetpackcomposedummyproject.note.domain.use_cases

import com.spider.jetpackcomposedummyproject.note.domain.model.Note
import com.spider.jetpackcomposedummyproject.note.domain.repository.NoteRepository

class DeleteNote(
    private val repository: NoteRepository
) {

    suspend operator fun invoke(note: Note){
        repository.deleteNote(note)
    }
}