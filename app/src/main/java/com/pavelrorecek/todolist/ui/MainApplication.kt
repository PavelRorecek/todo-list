package com.pavelrorecek.todolist.ui

import android.app.Application
import android.content.Context
import com.pavelrorecek.todolist.data.TodoEoMapper
import com.pavelrorecek.todolist.data.TodoListRepositoryImpl
import com.pavelrorecek.todolist.domain.AddTodoUseCase
import com.pavelrorecek.todolist.domain.CompleteTodoUseCase
import com.pavelrorecek.todolist.domain.DeleteTodoUseCase
import com.pavelrorecek.todolist.domain.ObserveTodoListUseCase
import com.pavelrorecek.todolist.domain.TodoListRepository
import com.pavelrorecek.todolist.presentation.ListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)

            modules(
                module {
                    factory<Json> { Json }
                    factoryOf(::TodoEoMapper)
                    single {
                        TodoListRepositoryImpl(
                            preferences = get<Context>().getSharedPreferences(
                                "",
                                Context.MODE_PRIVATE,
                            ),
                            scope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
                            json = get(),
                            mapper = get(),
                        )
                    } bind TodoListRepository::class
                    factoryOf(::AddTodoUseCase)
                    factoryOf(::CompleteTodoUseCase)
                    factoryOf(::DeleteTodoUseCase)
                    factoryOf(::ObserveTodoListUseCase)
                    viewModelOf(::ListViewModel)
                },
            )
        }
    }
}
