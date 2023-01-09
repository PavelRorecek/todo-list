package com.pavelrorecek.todolist.domain

import com.pavelrorecek.todolist.model.Todo

internal class DeleteTodoUseCase(private val repository: TodoListRepository) {

    operator fun invoke(id: Todo.Id) {
        repository.delete(id)
    }
}
