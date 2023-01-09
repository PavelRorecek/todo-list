package com.pavelrorecek.todolist.data

import com.pavelrorecek.todolist.data.TodoEo.StatusEo.NOT_DONE

internal fun todoEo(
    id: Int = 42,
    title: String = "title",
    status: TodoEo.StatusEo = NOT_DONE,
) = TodoEo(id = id, title = title, status = status)
