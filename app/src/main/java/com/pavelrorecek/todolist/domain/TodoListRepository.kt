package com.pavelrorecek.todolist.domain

import com.pavelrorecek.todolist.model.Todo
import kotlinx.coroutines.flow.Flow

internal interface TodoListRepository {
    fun add(title: Todo.Title)
    fun complete(id: Todo.Id)
    fun delete(id: Todo.Id)
    fun observe(): Flow<List<Todo>>
}
