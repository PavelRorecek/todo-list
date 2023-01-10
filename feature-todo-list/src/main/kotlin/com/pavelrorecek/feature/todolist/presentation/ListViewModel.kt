package com.pavelrorecek.feature.todolist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pavelrorecek.feature.todolist.domain.AddTodoUseCase
import com.pavelrorecek.feature.todolist.domain.CompleteTodoUseCase
import com.pavelrorecek.feature.todolist.domain.DeleteTodoUseCase
import com.pavelrorecek.feature.todolist.domain.ObserveTodoListUseCase
import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Action
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Color
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Decoration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ListViewModel(
    val observeTodoList: ObserveTodoListUseCase,
    val completeTodo: CompleteTodoUseCase,
    val deleteTodo: DeleteTodoUseCase,
    val addTodo: AddTodoUseCase,
) : ViewModel() {

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch {
            observeTodoList().collect { todoList ->
                _state.value = _state.value.copy(
                    todoItems = todoList.map(::toState),
                    isEmptyVisible = todoList.isEmpty(),
                    isListVisible = todoList.isNotEmpty(),
                )
            }
        }
    }

    private fun toState(todo: Todo) = State.TodoState(
        id = todo.id,
        title = todo.title.value,
        decoration = when (todo.status) {
            Todo.Status.NOT_DONE -> Decoration.NONE
            Todo.Status.DONE -> Decoration.STRIKE_THROUGH
        },
        titleColor = when (todo.status) {
            Todo.Status.NOT_DONE -> Color.PRIMARY
            Todo.Status.DONE -> Color.SECONDARY
        },
        action = when (todo.status) {
            Todo.Status.NOT_DONE -> Action.COMPLETE
            Todo.Status.DONE -> Action.DELETE
        },
        actionColor = when (todo.status) {
            Todo.Status.NOT_DONE -> Color.PRIMARY
            Todo.Status.DONE -> Color.SECONDARY
        },
    )

    fun onComplete(id: Todo.Id) {
        completeTodo(id)
    }

    fun onDelete(id: Todo.Id) {
        deleteTodo(id)
    }

    fun onNewTodoTitle(title: String) {
        _state.value = _state.value.copy(
            newTodoTitle = title,
            isAddVisible = title.isNotBlank(),
        )
    }

    fun onNewTodoAdd() {
        addTodo(_state.value.newTodoTitle.let(Todo::Title))
        _state.value = _state.value.copy(
            newTodoTitle = "",
            isAddVisible = false,
        )
    }

    data class State(
        val todoItems: List<TodoState> = emptyList(),
        val newTodoTitle: String = "",
        val isAddVisible: Boolean = false,
        val isEmptyVisible: Boolean = false,
        val isListVisible: Boolean = false,
    ) {

        data class TodoState(
            val id: Todo.Id,
            val title: String,
            val decoration: Decoration,
            val titleColor: Color,
            val action: Action,
            val actionColor: Color,
        ) {

            enum class Decoration { NONE, STRIKE_THROUGH }
            enum class Color { PRIMARY, SECONDARY }
            enum class Action { COMPLETE, DELETE }
        }
    }
}
