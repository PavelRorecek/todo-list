package com.pavelrorecek.feature.todolist.data

import android.content.SharedPreferences
import com.pavelrorecek.core.test.TestDispatcherRule
import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.model.todo
import com.pavelrorecek.feature.todolist.model.todoId
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Rule
import org.junit.Test

internal class TodoListRepositoryImplTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @Test
    fun `should emit stored todo list`() = runRepositoryTest {
        val storedTodoEo: TodoEo = mockk()
        val storedTodo: Todo = mockk()
        val storedTodoListEoSerialized = "serialized"
        val preferences = mockPreferences {
            every { getString(KEY, any()) } returns storedTodoListEoSerialized
        }
        val mapper: TodoEoMapper = mockk {
            every { toDomain(storedTodoEo) } returns storedTodo
        }
        val json: Json = mockk {
            every { decodeFromString<List<TodoEo>>(storedTodoListEoSerialized) } returns listOf(
                storedTodoEo,
            )
        }
        val repository = repository(
            preferences = preferences,
            mapper = mapper,
            json = json,
        )

        repository.observe().first() shouldBe listOf(storedTodo)
    }

    @Test
    fun `should add todo to list`() = runRepositoryTest {
        val storedTodoEo: TodoEo = mockk()
        val storedTodo: Todo = todo(id = todoId(42))
        val storedTodoListEoSerialized = "stored_serialized"

        val todoListEoToStoreSerialized = "to_store_serialized"
        val todoToStore = Todo(
            id = Todo.Id(43),
            title = Todo.Title("Title"),
            status = Todo.Status.NOT_DONE,
        )
        val todoEoToStore = todoEo()

        val editor: SharedPreferences.Editor = mockPreferencesEditor()
        val preferences = mockPreferences {
            every { getString(KEY, any()) } returns storedTodoListEoSerialized
            every { edit() } returns editor
        }
        val mapper: TodoEoMapper = mockk {
            every { toDomain(storedTodoEo) } returns storedTodo
            every { toData(storedTodo) } returns storedTodoEo
            every { toData(todoToStore) } returns todoEoToStore
        }
        val json: Json = mockk {
            every { decodeFromString<List<TodoEo>>(storedTodoListEoSerialized) } returns listOf(
                storedTodoEo,
            )
            every {
                encodeToString(
                    listOf(
                        storedTodoEo,
                        todoEoToStore,
                    ),
                )
            } returns todoListEoToStoreSerialized
        }
        val repository = repository(
            preferences = preferences,
            mapper = mapper,
            json = json,
        )

        repository.add(Todo.Title("Title"))

        verify {
            editor.putString(KEY, todoListEoToStoreSerialized)
            editor.apply()
        }
    }

    @Test
    fun `should complete todo`() = runRepositoryTest {
        val storedTodoEo: TodoEo = mockk()
        val storedTodoId = Todo.Id(42)
        val storedTodo = todo(id = storedTodoId)
        val storedTodoListEoSerialized = "stored_serialized"

        val todoListEoToStoreSerialized = "to_store_serialized"
        val todoToStore = storedTodo.copy(status = Todo.Status.DONE)
        val todoEoToStore = todoEo()

        val editor: SharedPreferences.Editor = mockPreferencesEditor()
        val preferences = mockPreferences {
            every { getString(KEY, any()) } returns storedTodoListEoSerialized
            every { edit() } returns editor
        }
        val mapper: TodoEoMapper = mockk {
            every { toDomain(storedTodoEo) } returns storedTodo
            every { toData(storedTodo) } returns storedTodoEo
            every { toData(todoToStore) } returns todoEoToStore
        }
        val json: Json = mockk {
            every { decodeFromString<List<TodoEo>>(storedTodoListEoSerialized) } returns listOf(
                storedTodoEo,
            )
            every { encodeToString(listOf(todoEoToStore)) } returns todoListEoToStoreSerialized
        }
        val repository = repository(
            preferences = preferences,
            mapper = mapper,
            json = json,
        )

        repository.complete(storedTodoId)

        verify {
            editor.putString(KEY, todoListEoToStoreSerialized)
            editor.apply()
        }
    }

    @Test
    fun `should delete todo`() = runRepositoryTest {
        val storedTodoEo: TodoEo = mockk()
        val storedTodoId = Todo.Id(42)
        val storedTodo = todo(id = storedTodoId)
        val storedTodoListEoSerialized = "stored_serialized"

        val todoListEoToStoreSerialized = "to_store_serialized"

        val editor: SharedPreferences.Editor = mockPreferencesEditor()
        val preferences = mockPreferences {
            every { getString(KEY, any()) } returns storedTodoListEoSerialized
            every { edit() } returns editor
        }
        val mapper: TodoEoMapper = mockk {
            every { toDomain(storedTodoEo) } returns storedTodo
        }
        val json: Json = mockk {
            every { decodeFromString<List<TodoEo>>(storedTodoListEoSerialized) } returns listOf(
                storedTodoEo,
            )
            every { encodeToString<List<TodoEo>>(emptyList()) } returns todoListEoToStoreSerialized
        }
        val repository = repository(
            preferences = preferences,
            mapper = mapper,
            json = json,
        )

        repository.delete(storedTodoId)

        verify {
            editor.putString(KEY, todoListEoToStoreSerialized)
            editor.apply()
        }
    }

    private fun runRepositoryTest(
        testBody: suspend TestScope.() -> Unit,
    ) {
        try {
            runTest {
                testBody()
                // Cancel manually because it is not closed because of the subscriptionCount logic.
                cancel()
            }
        } catch (e: CancellationException) {
            // Pass
        }
    }

    private fun mockPreferencesEditor(
        block: SharedPreferences.Editor.() -> Unit = {},
    ): SharedPreferences.Editor = mockk(relaxUnitFun = true) {
        every { putString(KEY, any()) } returns this
        block()
    }

    private fun mockPreferences(
        block: SharedPreferences.() -> Unit = {},
    ): SharedPreferences = mockk(relaxUnitFun = true) {
        every { getString(KEY, any()) } returns ""
        block()
    }

    private fun CoroutineScope.repository(
        preferences: SharedPreferences = mockPreferences(),
        json: Json = mockk(),
        mapper: TodoEoMapper = mockk(),
    ) = TodoListRepositoryImpl(
        preferences = preferences,
        scope = this,
        json = json,
        mapper = mapper,
    )

    private companion object {
        const val KEY = "todo_list"
    }
}
