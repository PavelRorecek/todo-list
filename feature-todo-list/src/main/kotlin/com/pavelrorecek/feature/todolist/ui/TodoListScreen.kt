package com.pavelrorecek.feature.todolist.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pavelrorecek.core.design.AppTheme
import com.pavelrorecek.core.design.BaseScreen
import com.pavelrorecek.feature.todolist.R
import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.presentation.ListViewModel
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Action
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Color
import com.pavelrorecek.feature.todolist.presentation.ListViewModel.State.TodoState.Decoration
import org.koin.androidx.compose.koinViewModel

@Composable
fun TodoListScreen() {
    val viewModel: ListViewModel = koinViewModel()
    val state = viewModel.state.collectAsState().value

    TodoListScreen(
        state = state,
        onComplete = viewModel::onComplete,
        onDelete = viewModel::onDelete,
        onAdd = viewModel::onNewTodoAdd,
        onNewTodoTitle = viewModel::onNewTodoTitle,
    )
}

@Composable
internal fun TodoListScreen(
    state: ListViewModel.State,
    onComplete: (Todo.Id) -> Unit,
    onDelete: (Todo.Id) -> Unit,
    onAdd: () -> Unit,
    onNewTodoTitle: (String) -> Unit,
) {
    BaseScreen {
        val focusManager = LocalFocusManager.current

        Column(modifier = Modifier.fillMaxSize()) {
            if (state.isEmptyVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 32.dp),
                    contentAlignment = Center,
                ) {
                    Text(
                        text = stringResource(id = R.string.empty_todo_list),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
            if (state.isListVisible) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(vertical = 16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    state.todoItems.forEach { todo ->
                        TodoCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            todo = todo,
                            onComplete = { onComplete(todo.id) },
                            onDelete = { onDelete(todo.id) },
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                verticalAlignment = CenterVertically,
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
                    value = state.newTodoTitle,
                    onValueChange = onNewTodoTitle,
                    placeholder = {
                        Text(
                            color = MaterialTheme.colorScheme.secondary,
                            text = stringResource(id = R.string.add_todo_placeholder),
                        )
                    },
                )
                val addButtonSize = 48.dp
                Box(
                    modifier = Modifier
                        .height(addButtonSize)
                        .animateContentSize(),
                ) {
                    if (state.isAddVisible) {
                        IconButton(
                            modifier = Modifier
                                .padding(start = 8.dp, end = 4.dp)
                                .size(addButtonSize),
                            onClick = {
                                onAdd()
                                focusManager.clearFocus()
                            },
                        ) {
                            Icon(
                                modifier = Modifier.padding(8.dp),
                                painter = painterResource(id = R.drawable.add),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun TodoCard(
    modifier: Modifier,
    todo: TodoState,
    onComplete: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(4.dp),
            ),
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .align(CenterVertically),
            text = todo.title,
            textDecoration = when (todo.decoration) {
                Decoration.NONE -> TextDecoration.None
                Decoration.STRIKE_THROUGH -> TextDecoration.LineThrough
            },
            color = when (todo.titleColor) {
                Color.PRIMARY -> MaterialTheme.colorScheme.primary
                Color.SECONDARY -> MaterialTheme.colorScheme.secondary
            },
        )
        val actionColor = when (todo.actionColor) {
            Color.PRIMARY -> MaterialTheme.colorScheme.primary
            Color.SECONDARY -> MaterialTheme.colorScheme.secondary
        }
        when (todo.action) {
            Action.COMPLETE -> IconButton(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Bottom),
                onClick = { onComplete() },
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.done),
                    contentDescription = null,
                    tint = actionColor,
                )
            }

            Action.DELETE -> IconButton(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .align(Bottom),
                onClick = { onDelete() },
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = null,
                    tint = actionColor,
                )
            }
        }
    }
}

@Preview
@Composable
private fun TodoListWithoutItems() {
    AppTheme {
        TodoListScreen(
            state = ListViewModel.State(
                todoItems = emptyList(),
                newTodoTitle = "Do something",
                isAddVisible = true,
                isEmptyVisible = true,
                isListVisible = false,
            ),
            onComplete = {},
            onDelete = {},
            onAdd = {},
            onNewTodoTitle = {},
        )
    }
}

@Preview
@Composable
private fun TodoListWithItems() {
    AppTheme {
        TodoListScreen(
            state = ListViewModel.State(
                todoItems = listOf(
                    TodoState(
                        id = Todo.Id(value = 0),
                        title = "Wash the dishes",
                        decoration = Decoration.NONE,
                        titleColor = Color.PRIMARY,
                        action = Action.COMPLETE,
                        actionColor = Color.PRIMARY,
                    ),
                    TodoState(
                        id = Todo.Id(value = 0),
                        title = "Mop the floor",
                        decoration = Decoration.STRIKE_THROUGH,
                        titleColor = Color.SECONDARY,
                        action = Action.DELETE,
                        actionColor = Color.SECONDARY,
                    ),
                    TodoState(
                        id = Todo.Id(value = 0),
                        title = "Mop the floor\nand this\nand that",
                        decoration = Decoration.STRIKE_THROUGH,
                        titleColor = Color.SECONDARY,
                        action = Action.DELETE,
                        actionColor = Color.SECONDARY,
                    ),
                ),
                newTodoTitle = "Do something",
                isAddVisible = true,
                isListVisible = true,
                isEmptyVisible = false,
            ),
            onComplete = {},
            onDelete = {},
            onAdd = {},
            onNewTodoTitle = {},
        )
    }
}
