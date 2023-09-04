package com.example.todolist

data class Task(
    val id: Long,
    val name: String,
    val description: String,
    val date: String,
    val taskNumber: Int// You can use a String or another data type for dates
)

