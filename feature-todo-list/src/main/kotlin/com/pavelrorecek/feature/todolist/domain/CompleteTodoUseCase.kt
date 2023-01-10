package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.Todo

internal class CompleteTodoUseCase(private val repository: TodoListRepository) {

    operator fun invoke(id: Todo.Id) {
        repository.complete(id)
    }
}
