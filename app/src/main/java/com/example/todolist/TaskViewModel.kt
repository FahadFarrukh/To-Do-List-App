//package com.example.todolist
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//class TaskViewModel(private val repository: TaskRepository) : ViewModel() {
//
//    fun deleteTaskById(taskId: Long) {
//        viewModelScope.launch(Dispatchers.IO) {
//            Db.taskDao().deleteTaskById(taskId)
//        }
//    }
//
//}
