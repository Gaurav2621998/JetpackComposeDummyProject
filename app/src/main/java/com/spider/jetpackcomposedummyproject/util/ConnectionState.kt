package com.spider.jetpackcomposedummyproject.util

sealed class ConnectionState{
    object Available:ConnectionState()
    object Unavailable:ConnectionState()
}
