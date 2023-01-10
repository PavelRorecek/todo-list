package com.pavelrorecek.feature.todolist.data

import com.pavelrorecek.feature.todolist.model.Todo

internal class TodoEoMapper {

    fun toData(domain: Todo) = TodoEo(
        id = domain.id.value,
        title = domain.title.value,
        status = when (domain.status) {
            Todo.Status.DONE -> TodoEo.StatusEo.DONE
            Todo.Status.NOT_DONE -> TodoEo.StatusEo.NOT_DONE
        },
    )

    fun toDomain(eo: TodoEo) = Todo(
        id = Todo.Id(eo.id),
        title = Todo.Title(eo.title),
        status = when (eo.status) {
            TodoEo.StatusEo.DONE -> Todo.Status.DONE
            TodoEo.StatusEo.NOT_DONE -> Todo.Status.NOT_DONE
        },
    )
}
