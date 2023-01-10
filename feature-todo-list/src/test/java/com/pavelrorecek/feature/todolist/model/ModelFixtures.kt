package com.pavelrorecek.feature.todolist.model

import com.pavelrorecek.feature.todolist.model.Todo.Status.NOT_DONE

internal fun todoId(
    value: Int = 42,
) = Todo.Id(value)

internal fun todoTitle(
    value: String = "title",
) = Todo.Title(value)

internal fun todo(
    id: Todo.Id = todoId(),
    title: Todo.Title = todoTitle(),
    status: Todo.Status = NOT_DONE,
) = Todo(id = id, title = title, status = status)
