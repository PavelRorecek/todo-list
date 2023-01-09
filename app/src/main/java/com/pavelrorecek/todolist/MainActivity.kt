package com.pavelrorecek.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pavelrorecek.todolist.ui.TodoListScreen
import com.pavelrorecek.todolist.ui.theme.TodoListTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListTheme {
                TodoListScreen()
            }
        }
    }
}

