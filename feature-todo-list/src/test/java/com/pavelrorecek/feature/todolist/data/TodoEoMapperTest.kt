package com.pavelrorecek.feature.todolist.data

import com.pavelrorecek.feature.todolist.model.Todo
import com.pavelrorecek.feature.todolist.model.todo
import io.kotest.matchers.shouldBe
import org.junit.Test

internal class TodoEoMapperTest {

    @Test
    fun `should map domain id to eo id`() {
        val mapper = mapper()
        val domain = todo(id = Todo.Id(value = 42))

        mapper.toData(domain).id shouldBe 42
    }

    @Test
    fun `should map domain title to eo title`() {
        val mapper = mapper()
        val domain = todo(title = Todo.Title(value = "title"))

        mapper.toData(domain).title shouldBe "title"
    }

    @Test
    fun `should map not-done domain status to eo status`() {
        val mapper = mapper()
        val domain = todo(status = Todo.Status.NOT_DONE)

        mapper.toData(domain).status shouldBe TodoEo.StatusEo.NOT_DONE
    }

    @Test
    fun `should map done domain status to eo status`() {
        val mapper = mapper()
        val domain = todo(status = Todo.Status.DONE)

        mapper.toData(domain).status shouldBe TodoEo.StatusEo.DONE
    }

    @Test
    fun `should map eo id to domain id`() {
        val mapper = mapper()
        val eo = todoEo(id = 42)

        mapper.toDomain(eo).id shouldBe Todo.Id(value = 42)
    }

    @Test
    fun `should map eo title to domain title`() {
        val mapper = mapper()
        val eo = todoEo(title = "title")

        mapper.toDomain(eo).title shouldBe Todo.Title(value = "title")
    }

    @Test
    fun `should map not-done eo status to domain status`() {
        val mapper = mapper()
        val eo = todoEo(status = TodoEo.StatusEo.NOT_DONE)

        mapper.toDomain(eo).status shouldBe Todo.Status.NOT_DONE
    }

    @Test
    fun `should map done eo status to domain status`() {
        val mapper = mapper()
        val eo = todoEo(status = TodoEo.StatusEo.DONE)

        mapper.toDomain(eo).status shouldBe Todo.Status.DONE
    }

    private fun mapper() = TodoEoMapper()
}
