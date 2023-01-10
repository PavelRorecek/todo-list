package com.pavelrorecek.feature.todolist.data

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import com.pavelrorecek.feature.todolist.domain.TodoListRepository
import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.model.Todo.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class TodoListRepositoryImpl(
    private val preferences: SharedPreferences,
    scope: CoroutineScope,
    private val json: Json,
    private val mapper: TodoEoMapper,
) : TodoListRepository {

    private val todoList: MutableStateFlow<List<Todo>> =
        MutableStateFlow(loadFromPreferences())

    init {
        val listener = OnSharedPreferenceChangeListener { _, _ ->
            todoList.value = loadFromPreferences()
        }

        todoList.subscriptionCount.map { it > 0 }.distinctUntilChanged().map { isCollected ->
            if (isCollected) {
                preferences.registerOnSharedPreferenceChangeListener(listener)
            } else {
                preferences.unregisterOnSharedPreferenceChangeListener(listener)
            }
        }.launchIn(scope)
    }

    private fun loadFromPreferences(): List<Todo> = preferences.getString(KEY, null)
        ?.let { json.decodeFromString<List<TodoEo>>(it) }
        ?.map(mapper::toDomain)
        ?: emptyList()

    private fun storeToPreferences(todoList: List<Todo>) {
        preferences.edit()
            .putString(KEY, json.encodeToString(todoList.map(mapper::toData)))
            .apply()
    }

    override fun add(title: Todo.Title) {
        storeToPreferences(
            todoList.value + Todo(
                id = Todo.Id((todoList.value.maxOfOrNull { it.id.value } ?: 0) + 1),
                title = title,
                status = Status.NOT_DONE,
            ),
        )
    }

    override fun complete(id: Todo.Id) {
        storeToPreferences(
            todoList.value.map { todo ->
                if (todo.id == id) todo.copy(status = Status.DONE) else todo
            },
        )
    }

    override fun delete(id: Todo.Id) {
        storeToPreferences(todoList.value.filterNot { todo -> todo.id == id })
    }

    override fun observe(): Flow<List<Todo>> = todoList

    private companion object {
        const val KEY = "todo_list"
    }
}
