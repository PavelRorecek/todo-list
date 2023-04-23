package com.pavelrorecek.feature.todolist.platform

import android.content.Context
import com.pavelrorecek.feature.todolist.R
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class ListStringsImplTest {

    @Test
    fun `should return empty string`() {
        val context: Context = mockk {
            every { getString(R.string.empty_todo_list) } returns "Empty"
        }
        val strings = ListStringsImpl(context)

        strings.empty() shouldBe "Empty"
    }

    @Test
    fun `should return placeholder string`() {
        val context: Context = mockk {
            every { getString(R.string.add_todo_placeholder) } returns "Placeholder"
        }
        val strings = ListStringsImpl(context)

        strings.addTodoPlaceholder() shouldBe "Placeholder"
    }
}
