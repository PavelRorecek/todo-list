package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.Todo
import kotlinx.coroutines.flow.Flow

internal interface TodoListRepository {
    fun add(title: Todo.Title)
    fun complete(id: Todo.Id)
    fun delete(id: Todo.Id)
    fun observe(): Flow<List<Todo>>
}
