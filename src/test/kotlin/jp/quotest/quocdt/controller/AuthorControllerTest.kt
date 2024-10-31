package jp.quotest.quocdt.controller

import jp.quotest.quocdt.db.tables.records.AuthorsRecord
import jp.quotest.quocdt.service.AuthorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.junit.jupiter.api.Test
import java.time.LocalDate
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.http.MediaType

@WebMvcTest(AuthorController::class)
class AuthorControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authorService: AuthorService

    @Test
    fun `getAllAuthors should return list of authors`() {

        val authors = listOf(
            AuthorsRecord(1, "Author One", LocalDate.of(1990, 1, 1)),
            AuthorsRecord(2, "Author Two", LocalDate.of(1986, 5, 5))
        )
        `when`(authorService.getAllAuthors()).thenReturn(authors)

        mockMvc.perform(get("/api/author"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Author One"))
            .andExpect(jsonPath("$[0].birthDate").value("1990-01-01"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Author Two"))
            .andExpect(jsonPath("$[1].birthDate").value("1986-05-05"))
    }

    @Test
    fun `createAuthor should return success message`() {
        // モック動作の定義
        `when`(authorService.createAuthor("New Author", LocalDate.of(1990, 1, 1))).thenReturn( AuthorsRecord(1, "New Author", LocalDate.of(1990, 1, 1)))

        // テスト実行
        mockMvc.perform(post("/api/author/create")
            .param("name", "New Author")
            .param("birthday", "1990-01-01"))
            .andExpect(status().isOk)
            .andExpect(content().string("Create author success"))
    }

    @Test
    fun `createAuthor should return failure message if creation fails`() {
        // モック動作の定義
        `when`(authorService.createAuthor("Failed Author", LocalDate.of(2000, 12, 31))).thenReturn(null)

        // テスト実行
        mockMvc.perform(post("/api/author/create")
            .param("name", "Failed Author")
            .param("birthday", "2000-12-31"))
            .andExpect(status().isOk)
            .andExpect(content().string("Create author failed"))
    }
}