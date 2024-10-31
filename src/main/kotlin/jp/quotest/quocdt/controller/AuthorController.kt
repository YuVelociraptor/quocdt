package jp.quotest.quocdt.controller

import jp.quotest.quocdt.response.AuthorResponse
import jp.quotest.quocdt.service.AuthorService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/author")
class AuthorController(private val authorService: AuthorService) {

    @GetMapping
    fun getAllAuthors(): List<AuthorResponse> {

        return authorService.getAllAuthors().map { author ->
            AuthorResponse(
                id = author.id,
                name = author.name,
                birthDate = author.birthDate
            )
        }
    }

    @PostMapping("/create")
    fun createAuthor(
        @RequestParam("name") name: String,
        @RequestParam("birthday") birthday: String
    ): String {

        val birthDate = LocalDate.parse(birthday)

        if (!birthDate.isBefore(LocalDate.now())) {
            return "Error: Birthday must be before today."
        }

        authorService.createAuthor(name, birthDate) ?: return "Create author failed"
        return "Create author success"
    }

    @PostMapping("/update")
    fun updateAuthor(
        @RequestParam("id") id: Int,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("birthday", required = false) birthday: String?
    ): String {

        val author = authorService.getAuthorById(id) ?: return "Author not found"

        val n = name ?: author.name
        val b = birthday ?: author.birthDate.toString()

        val birthDate = LocalDate.parse(b)
        if (!birthDate.isBefore(LocalDate.now())) {
            return "Error: Birthday must be before today."
        }

        authorService.updateAuthor(id, n, birthDate) ?: return "Update author failed"
        return "Update author success"
    }
}