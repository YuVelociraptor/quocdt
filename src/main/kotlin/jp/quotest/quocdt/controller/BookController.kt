package jp.quotest.quocdt.controller

import jp.quotest.quocdt.response.BookWithAuthorsResponse
import jp.quotest.quocdt.service.BookService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/book")
class BookController(private val bookService: BookService) {

    @GetMapping
    fun getAllBooks(): List<BookWithAuthorsResponse> {
        return bookService.getAllBooksWithAuthors()
    }

    @PostMapping("/create")
    fun createBook(
        @RequestParam("title") title: String,
        @RequestParam("price") price: Long,
        @RequestParam("publicationStatus") publicationStatus: Boolean,
        @RequestParam("authorIds") authorIds: List<Int>
    ): String {

        if(price < 0) {
            return "Error: Price must be greater than 0."
        }

        if(authorIds.size < 1) {
            return "Error: Book must have at least 1 author."

        }

        bookService.createBookWithAuthors(title, price, publicationStatus, authorIds) ?: return "Create book failed"
        return "Create book success"
    }

    @PostMapping("/update")
    fun updateBook(
        @RequestParam("id") id: Int,
        @RequestParam("title", required = false) title: String?,
        @RequestParam("price", required = false) price: Long?,
        @RequestParam("publicationStatus", required = false) publicationStatus: Boolean?,
        @RequestParam("authorIds", required = false) authorIds: List<Int>?
    ): String {
        val book = bookService.getBookWithAuthorsById(id) ?: return "Book not found"

        val t = title ?: book.title
        val p = price ?: book.price
        val ps = publicationStatus ?: book.publicationStatus
        val a = authorIds ?: book.authors.map { it.id }

        if(p < 0) {
            return "Error: Price must be greater than 0."
        }

        if(a.size < 1) {
            return "Error: Book must have at least 1 author."

        }

        bookService.updateBookWithAuthors(id, t, p, ps, a) ?: return "Update book failed"
        return "Update book success"
    }
}