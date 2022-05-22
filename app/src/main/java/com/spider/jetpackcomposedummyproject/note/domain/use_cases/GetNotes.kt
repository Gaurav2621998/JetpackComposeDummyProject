package com.spider.jetpackcomposedummyproject.note.domain.use_cases

import com.spider.jetpackcomposedummyproject.note.domain.model.Note
import com.spider.jetpackcomposedummyproject.note.domain.repository.NoteRepository
import com.spider.jetpackcomposedummyproject.note.domain.util.NoteOrder
import com.spider.jetpackcomposedummyproject.note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotes(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteOrder: NoteOrder): Flow<List<Note>> {
        return noteRepository.getNotes().map { notes ->
            when(noteOrder.orderType){
                is OrderType.Ascending ->{
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedBy { it.title.lowercase() }
                        is NoteOrder.Color -> notes.sortedBy { it.color }
                        is NoteOrder.Date -> notes.sortedBy { it.timestamp }
                    }

                }
                is OrderType.Descending ->{
                    when(noteOrder){
                        is NoteOrder.Title -> notes.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Color -> notes.sortedByDescending { it.color }
                        is NoteOrder.Date -> notes.sortedByDescending { it.timestamp }
                    }
                }
            }
        }
    }
}