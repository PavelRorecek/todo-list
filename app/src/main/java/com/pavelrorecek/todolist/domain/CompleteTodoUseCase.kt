package com.pavelrorecek.todolist.domain

import com.pavelrorecek.todolist.model.Todo

internal class CompleteTodoUseCase(private val repository: TodoListRepository) {

    operator fun invoke(id: Todo.Id) {
        repository.complete(id)
    }
}
