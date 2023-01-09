package com.pavelrorecek.todolist.model

internal data class Todo(
    val id: Id,
    val title: Title,
    val status: Status,
) {
    @JvmInline
    value class Id(val value: Int)

    @JvmInline
    value class Title(val value: String)

    enum class Status { NOT_DONE, DONE }
}
