package com.pavelrorecek.feature.todolist.presentation

import com.pavelrorecek.core.test.TestDispatcherRule
import com.pavelrorecek.feature.todolist.domain.AddTodoUseCase
import com.pavelrorecek.feature.todolist.domain.CompleteTodoUseCase
import com.pavelrorecek.feature.todolist.domain.DeleteTodoUseCase
import com.pavelrorecek.feature.todolist.domain.ObserveTodoListUseCase
import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.model.todo
import com.pavelrorecek.feature.todolist.model.todoId
import com.pavelrorecek.feature.todolist.model.todoTitle
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Decoration
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class ListViewModelTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Test
    fun `should map empty string to state`() = runTest {
        val viewModel = viewModel(
            strings = mockk(relaxed = true) { every { empty() } returns "Empty" },
        )

        viewModel.state.first().emptyMessage shouldBe "Empty"
    }

    @Test
    fun `should map placeholder string to state`() = runTest {
        val viewModel = viewModel(
            strings = mockk(relaxed = true) { every { addTodoPlaceholder() } returns "Wash the dishes" },
        )

        viewModel.state.first().addTodoPlaceholder shouldBe "Wash the dishes"
    }

    @Test
    fun `should show message when todo list is empty`() = runTest {
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(emptyList()) },
        )

        viewModel.state.first().isEmptyVisible shouldBe true
    }

    @Test
    fun `should hide message when todo list is not empty`() = runTest {
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo())) },
        )

        viewModel.state.first().isEmptyVisible shouldBe false
    }

    @Test
    fun `should show list when todo list is not empty`() = runTest {
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo())) },
        )

        viewModel.state.first().isListVisible shouldBe true
    }

    @Test
    fun `should hide list when todo list is empty`() = runTest {
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(emptyList()) },
        )

        viewModel.state.first().isListVisible shouldBe false
    }

    @Test
    fun `should map todo id to state`() = runTest {
        val id = todoId()
        val todo = todo(id = id)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().id shouldBe id
    }

    @Test
    fun `should map todo title to state`() = runTest {
        val todoTitle = todoTitle(value = "title")
        val todo = todo(title = todoTitle)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().title shouldBe "title"
    }

    @Test
    fun `should map not-done todo to decoration`() = runTest {
        val todo = todo(status = Todo.Status.NOT_DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().decoration shouldBe Decoration.NONE
    }

    @Test
    fun `should map done todo to decoration`() = runTest {
        val todo = todo(status = Todo.Status.DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().decoration shouldBe Decoration.STRIKE_THROUGH
    }

    @Test
    fun `should map not-done todo to title color`() = runTest {
        val todo = todo(status = Todo.Status.NOT_DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().titleColor shouldBe TodoState.Color.PRIMARY
    }

    @Test
    fun `should map done todo to title color`() = runTest {
        val todo = todo(status = Todo.Status.DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().titleColor shouldBe TodoState.Color.SECONDARY
    }

    @Test
    fun `should map not-done todo to action`() = runTest {
        val todo = todo(status = Todo.Status.NOT_DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().action shouldBe TodoState.Action.COMPLETE
    }

    @Test
    fun `should map done todo to action`() = runTest {
        val todo = todo(status = Todo.Status.DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().action shouldBe TodoState.Action.DELETE
    }

    @Test
    fun `should map not-done todo to action color`() = runTest {
        val todo = todo(status = Todo.Status.NOT_DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().actionColor shouldBe TodoState.Color.PRIMARY
    }

    @Test
    fun `should map done todo to action color`() = runTest {
        val todo = todo(status = Todo.Status.DONE)
        val viewModel = viewModel(
            observeTodoList = mockk { every { this@mockk.invoke() } returns flowOf(listOf(todo)) },
        )

        viewModel.state.first().todoItems.single().actionColor shouldBe TodoState.Color.SECONDARY
    }

    @Test
    fun `should complete todo`() {
        val id = todoId()
        val completeTodo: CompleteTodoUseCase = mockk(relaxUnitFun = true)
        val viewModel = viewModel(completeTodo = completeTodo)

        viewModel.onComplete(id)

        verify { completeTodo(id) }
    }

    @Test
    fun `should delete todo`() {
        val id = todoId()
        val deleteTodo: DeleteTodoUseCase = mockk(relaxUnitFun = true)
        val viewModel = viewModel(deleteTodo = deleteTodo)

        viewModel.onDelete(id)

        verify { deleteTodo(id) }
    }

    @Test
    fun `should map new todo text to state`() = runTest {
        val viewModel = viewModel()

        viewModel.onNewTodoTitle("todo")

        viewModel.state.first().newTodoTitle shouldBe "todo"
    }

    @Test
    fun `should add todo`() {
        val addTodo: AddTodoUseCase = mockk(relaxUnitFun = true)
        val viewModel = viewModel(addTodo = addTodo)

        viewModel.onNewTodoTitle("todo")
        viewModel.onNewTodoAdd()

        verify { addTodo(Todo.Title("todo")) }
    }

    @Test
    fun `should remove new todo title when it is added`() = runTest {
        val addTodo: AddTodoUseCase = mockk(relaxUnitFun = true)
        val viewModel = viewModel(addTodo = addTodo)

        viewModel.onNewTodoTitle("todo")
        viewModel.onNewTodoAdd()

        viewModel.state.first().newTodoTitle shouldBe ""
    }

    @Test
    fun `should enable add todo when title is not empty `() = runTest {
        val viewModel = viewModel()

        viewModel.onNewTodoTitle("todo")

        viewModel.state.first().isAddVisible shouldBe true
    }

    @Test
    fun `should hide add todo when title is empty`() = runTest {
        val viewModel = viewModel()

        viewModel.onNewTodoTitle("")

        viewModel.state.first().isAddVisible shouldBe false
    }

    @Test
    fun `should hide add todo when title is blank`() = runTest {
        val viewModel = viewModel()

        viewModel.onNewTodoTitle("     ")

        viewModel.state.first().isAddVisible shouldBe false
    }

    @Test
    fun `should hide add todo when it is added`() = runTest {
        val viewModel = viewModel()

        viewModel.onNewTodoTitle("todo")
        viewModel.onNewTodoAdd()

        viewModel.state.first().isAddVisible shouldBe false
    }

    private fun viewModel(
        observeTodoList: ObserveTodoListUseCase = mockk {
            every { this@mockk.invoke() } returns emptyFlow()
        },
        completeTodo: CompleteTodoUseCase = mockk(relaxUnitFun = true),
        deleteTodo: DeleteTodoUseCase = mockk(relaxUnitFun = true),
        addTodo: AddTodoUseCase = mockk(relaxUnitFun = true),
        strings: ListStrings = mockk(relaxed = true),
    ) = ListViewModel(
        observeTodoList = observeTodoList,
        completeTodo = completeTodo,
        deleteTodo = deleteTodo,
        addTodo = addTodo,
        strings = strings,
    )
}
