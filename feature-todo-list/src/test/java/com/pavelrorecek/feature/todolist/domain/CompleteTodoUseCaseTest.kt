package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.todoId
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class CompleteTodoUseCaseTest {

    @Test
    fun `should complete todo via repository`() {
        val todoId = todoId()
        val repository: TodoListRepository = mockk(relaxUnitFun = true)
        val completeTodo = CompleteTodoUseCase(repository = repository)

        completeTodo(todoId)

        verify { repository.complete(todoId) }
    }
}
