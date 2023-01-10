package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.Todo
import kotlinx.coroutines.flow.Flow

internal class ObserveTodoListUseCase(private val repository: TodoListRepository) {

    operator fun invoke(): Flow<List<Todo>> = repository.observe()
}
