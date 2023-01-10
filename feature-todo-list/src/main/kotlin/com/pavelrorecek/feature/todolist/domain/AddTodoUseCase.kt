package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.Todo

internal class AddTodoUseCase(private val repository: TodoListRepository) {

    operator fun invoke(title: Todo.Title) {
        repository.add(title)
    }
}
