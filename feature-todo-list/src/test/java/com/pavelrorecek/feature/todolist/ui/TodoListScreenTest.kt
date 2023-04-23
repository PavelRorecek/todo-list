package com.pavelrorecek.feature.todolist.ui

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_5
import app.cash.paparazzi.Paparazzi
import com.pavelrorecek.core.design.AppTheme
import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Color
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Decoration
import org.junit.Rule
import org.junit.Test

internal class TodoListScreenTest {

    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = PIXEL_5,
        theme = "Theme.TodoList",
    )

    @Test
    fun `Empty list`() {
        paparazzi.snapshot {
            TodoListScreen(
                state = State(
                    newTodoTitle = "",
                    addTodoPlaceholder = "E.g.: Wash the dishes…",

                    isAddVisible = false,
                    isEmptyVisible = true,
                    isListVisible = false,
                    emptyMessage = "Your todo list is empty.",
                ),
            )
        }
    }

    @Test
    fun `Non empty list`() {
        paparazzi.snapshot {
            TodoListScreen(
                state = State(
                    newTodoTitle = "Vacuum the floors",
                    addTodoPlaceholder = "E.g.: Wash the dishes…",
                    isAddVisible = true,
                    isEmptyVisible = false,
                    todoItems = listOf(
                        TodoState(
                            id = Todo.Id(value = 0),
                            title = "Wash the dishes",
                            decoration = Decoration.NONE,
                            titleColor = Color.PRIMARY,
                            action = TodoState.Action.COMPLETE,
                            actionColor = Color.PRIMARY,
                        ),
                        TodoState(
                            id = Todo.Id(value = 1),
                            title = "Take out the trash",
                            decoration = Decoration.STRIKE_THROUGH,
                            titleColor = Color.SECONDARY,
                            action = TodoState.Action.DELETE,
                            actionColor = Color.SECONDARY,
                        ),
                    ),
                    isListVisible = true,
                    emptyMessage = "",
                ),
            )
        }
    }

    @Composable
    private fun TodoListScreen(state: State) {
        AppTheme {
            TodoListScreen(
                state = state,
                onComplete = {},
                onDelete = {},
                onAdd = { },
                onNewTodoTitle = {},
            )
        }
    }
}
