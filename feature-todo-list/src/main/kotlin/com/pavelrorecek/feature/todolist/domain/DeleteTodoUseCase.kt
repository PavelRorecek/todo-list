package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.Todo

internal class DeleteTodoUseCase(private val repository: TodoListRepository) {

    operator fun invoke(id: Todo.Id) {
        repository.delete(id)
    }
}
