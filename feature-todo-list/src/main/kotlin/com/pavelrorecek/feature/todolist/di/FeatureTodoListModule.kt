package com.pavelrorecek.feature.todolist.di

import android.content.Context
import com.pavelrorecek.feature.todolist.data.TodoEoMapper
import com.pavelrorecek.feature.todolist.data.TodoListRepositoryImpl
import com.pavelrorecek.feature.todolist.domain.AddTodoUseCase
import com.pavelrorecek.feature.todolist.domain.CompleteTodoUseCase
import com.pavelrorecek.feature.todolist.domain.DeleteTodoUseCase
import com.pavelrorecek.feature.todolist.domain.ObserveTodoListUseCase
import com.pavelrorecek.feature.todolist.domain.TodoListRepository
import com.pavelrorecek.feature.todolist.platform.ListStringsImpl
import com.pavelrorecek.feature.todolist.presentation.ListStrings
import com.pavelrorecek.feature.todolist.presentation.ListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val featureTodoListModule: Module = module {
    factory<Json> { Json }
    factoryOf(::TodoEoMapper)
    single {
        TodoListRepositoryImpl(
            preferences = get<Context>().getSharedPreferences(
                TODO_LIST_PREFERENCES,
                Context.MODE_PRIVATE,
            ),
            scope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
            json = Json,
            mapper = get(),
        )
    } bind TodoListRepository::class
    factoryOf(::AddTodoUseCase)
    factoryOf(::CompleteTodoUseCase)
    factoryOf(::DeleteTodoUseCase)
    factoryOf(::ObserveTodoListUseCase)
    factoryOf(::ListStringsImpl) bind ListStrings::class
    viewModelOf(::ListViewModel)
}

private const val TODO_LIST_PREFERENCES = "todo_list_preferences"
