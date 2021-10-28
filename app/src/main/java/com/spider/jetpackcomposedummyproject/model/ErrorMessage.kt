package com.spider.jetpackcomposedummyproject.model

import com.spider.jetpackcomposedummyproject.helper.ErrorTypes

data class ErrorMessage(var type:ErrorTypes,var message:String?=null)

