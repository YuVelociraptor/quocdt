package jp.quotest.quocdt.controller

import jp.quotest.quocdt.db.tables.records.BooksRecord
import jp.quotest.quocdt.response.BookWithAuthorsResponse
import jp.quotest.quocdt.service.BookService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(BookController::class)
class BookControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var bookService: BookService

    @Test
    fun `createBook should return success message`() {
        `when`(bookService.createBookWithAuthors("Sample Book", 1500, true, listOf(1, 2)))
            .thenReturn(BooksRecord(1, "Sample Book", 1500, true))

        mockMvc.perform(
            post("/api/book/create")
                .param("title", "Sample Book")
                .param("price", "1500")
                .param("publicationStatus", "true")
                .param("authorIds", "1", "2")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Create book success"))
    }

    @Test
    fun `createBook should return error when price is negative`() {
        mockMvc.perform(
            post("/api/book/create")
                .param("title", "Sample Book")
                .param("price", "-10")
                .param("publicationStatus", "true")
                .param("authorIds", "1", "2")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Error: Price must be greater than 0."))
    }

    @Test
    fun `createBook should return error when no authors are provided`() {
        mockMvc.perform(
            post("/api/book/create")
                .param("title", "Sample Book")
                .param("price", "1500")
                .param("publicationStatus", "true")
                .param("authorIds", "")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Error: Book must have at least 1 author."))
    }

    @Test
    fun `updateBook should return success message`() {
        val existingBook = BookWithAuthorsResponse(1, "Sample Book", 1500, true, mutableListOf())
        `when`(bookService.getBookWithAuthorsById(1)).thenReturn(existingBook)
        `when`(bookService.updateBookWithAuthors(1, "Updated Book", 1600, true, listOf(1, 2)))
            .thenReturn(BooksRecord(1, "Updated Book", 1600, true))

        mockMvc.perform(
            post("/api/book/update")
                .param("id", "1")
                .param("title", "Updated Book")
                .param("price", "1600")
                .param("publicationStatus", "true")
                .param("authorIds", "1", "2")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Update book success"))
    }

    @Test
    fun `updateBook should return error when book not found`() {
        `when`(bookService.getBookWithAuthorsById(1)).thenReturn(null)

        mockMvc.perform(
            post("/api/book/update")
                .param("id", "1")
                .param("title", "Updated Book")
                .param("price", "1600")
                .param("publicationStatus", "true")
                .param("authorIds", "1", "2")
        )
            .andExpect(status().isOk)
            .andExpect(content().string("Book not found"))
    }
}