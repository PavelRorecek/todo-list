package com.pavelrorecek.feature.todolist.platform

import android.content.Context
import com.pavelrorecek.feature.todolist.R
import com.pavelrorecek.feature.todolist.presentation.ListStrings

internal class ListStringsImpl(
    private val context: Context,
) : ListStrings {

    override fun empty() = context.getString(R.string.empty_todo_list)
    override fun addTodoPlaceholder() = context.getString(R.string.add_todo_placeholder)
}
