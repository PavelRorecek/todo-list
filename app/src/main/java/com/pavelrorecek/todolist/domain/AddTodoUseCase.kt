package com.pavelrorecek.todolist.domain

import com.pavelrorecek.todolist.model.Todo

internal class AddTodoUseCase(private val repository: TodoListRepository) {

    operator fun invoke(title: Todo.Title) {
        repository.add(title)
    }
}
