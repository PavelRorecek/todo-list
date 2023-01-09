package com.pavelrorecek.todolist.domain

import com.pavelrorecek.todolist.model.todoTitle
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

internal class AddTodoUseCaseTest {

    @Test
    fun `should add todo via repository`() {
        val title = todoTitle()
        val repository: TodoListRepository = mockk(relaxUnitFun = true)
        val addTodo = AddTodoUseCase(repository = repository)

        addTodo(title)

        verify { repository.add(title) }
    }
}
