package com.pavelrorecek.feature.todolist.data

import kotlinx.serialization.Serializable

@Serializable
internal data class TodoEo(
    val id: Int,
    val title: String,
    val status: StatusEo,
) {

    @Serializable
    enum class StatusEo { NOT_DONE, DONE }
}
