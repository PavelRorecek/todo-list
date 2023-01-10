package com.pavelrorecek.feature.todolist.domain

import com.pavelrorecek.feature.todolist.model.todo
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Test

internal class ObserveTodoListUseCaseTest {

    @Test
    fun `should observe todo list via repository`() {
        val todoList = flowOf(listOf(todo()))
        val repository: TodoListRepository = mockk {
            every { this@mockk.observe() } returns todoList
        }
        val observeTodoList = ObserveTodoListUseCase(repository = repository)

        observeTodoList() shouldBe todoList
    }
}
