package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.todoId
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class DeleteTodoUseCaseTest {

    @Test
    fun `should delete todo via repository`() {
        val todoId = todoId()
        val repository: TodoListRepository = mockk(relaxUnitFun = true)
        val deleteTodo = DeleteTodoUseCase(repository = repository)

        deleteTodo(todoId)

        verify { repository.delete(todoId) }
    }
}
