package com.spider.jetpackcomposedummyproject.note.domain.util

sealed class OrderType{
    object Ascending : OrderType()
    object Descending : OrderType()
}
