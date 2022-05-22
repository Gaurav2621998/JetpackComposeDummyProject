package com.spider.jetpackcomposedummyproject.note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spider.jetpackcomposedummyproject.ui.theme.*

@Entity
data class Note(
    var title:String,
    var subTitle:String,
    var timestamp: Long,
    var color:Int,
){
    @PrimaryKey(autoGenerate = true)
    val id:Long=0L

    companion object{
        val noteColors = listOf(RedOrange, LightGreen, Violet, BabyBlue, RedPink)
    }
}
