package jp.quotest.quocdt.controller

import jp.quotest.quocdt.service.AuthorService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/author")
class AuthorController(private val authorService: AuthorService) {

    @GetMapping
    fun getAllAuthors(): String {
        //return authorService.fetchAllAuthors()
        val a = authorService.getAllAuthors()

        for (i in a) {
            println(i.id)
            println(i.name)
        }
        return "Hello"
    }

    @GetMapping("/create")
    fun createAuthor(): String {
        authorService.createAuthor("QuocDT", LocalDate.now())
        return "Create author success"
    }
}