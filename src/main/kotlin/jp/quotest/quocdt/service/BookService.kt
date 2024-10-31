package jp.quotest.quocdt.service

import jp.quotest.quocdt.db.tables.Authors
import jp.quotest.quocdt.db.tables.BookAuthors
import jp.quotest.quocdt.db.tables.Books
import jp.quotest.quocdt.db.tables.records.BooksRecord
import jp.quotest.quocdt.response.AuthorResponse
import jp.quotest.quocdt.response.BookWithAuthorsResponse
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.stereotype.Service

@Service
class BookService(private val dsl: DSLContext) {

    fun getAllBooksWithAuthors(): List<BookWithAuthorsResponse> {
        val result = dsl
            .select(
                Books.BOOKS.ID,
                Books.BOOKS.TITLE,
                Books.BOOKS.PRICE,
                Books.BOOKS.PUBLICATION_STATUS,
                Authors.AUTHORS.ID,
                Authors.AUTHORS.NAME,
                Authors.AUTHORS.BIRTH_DATE
            )
            .from(Books.BOOKS)
            .leftJoin(BookAuthors.BOOK_AUTHORS).on(Books.BOOKS.ID.eq(BookAuthors.BOOK_AUTHORS.BOOK_ID))
            .leftJoin(Authors.AUTHORS).on(BookAuthors.BOOK_AUTHORS.AUTHOR_ID.eq(Authors.AUTHORS.ID))
            .fetch()

        return result.map { record ->
            BookWithAuthorsResponse(
                id = record.get(Books.BOOKS.ID),
                title = record.get(Books.BOOKS.TITLE),
                price = record.get(Books.BOOKS.PRICE),
                publicationStatus = record.get(Books.BOOKS.PUBLICATION_STATUS),
                authors = mutableListOf()
            )
        }
    }

    fun createBookWithAuthors(
        title: String,
        price: Long,
        publicationStatus: Boolean,
        authorIds: List<Int>
    ): BooksRecord? {
        return try {
            dsl.transactionResult { config ->
                val ctx = DSL.using(config)  // `DSL.using`でコンテキストを取得

                val bookRecord = ctx.insertInto(Books.BOOKS)
                    .set(Books.BOOKS.TITLE, title)
                    .set(Books.BOOKS.PRICE, price)
                    .set(Books.BOOKS.PUBLICATION_STATUS, publicationStatus)
                    .returning()
                    .fetchOne()

                val bookId = bookRecord?.id ?: throw Exception("Book creation failed")

                authorIds.forEach { authorId ->
                    ctx.insertInto(BookAuthors.BOOK_AUTHORS)
                        .set(BookAuthors.BOOK_AUTHORS.BOOK_ID, bookId)
                        .set(BookAuthors.BOOK_AUTHORS.AUTHOR_ID, authorId)
                        .execute()
                }

                bookRecord
            }
        } catch (e: Exception) {
            println("Error creating book and authors: ${e.message}")
            null
        }
    }

    fun updateBookWithAuthors(
        id: Int,
        title: String,
        price: Long,
        publicationStatus: Boolean,
        authorIds: List<Int>
    ): BooksRecord? {
        return try {
            dsl.transactionResult { config ->
                val ctx = DSL.using(config)

                ctx.update(Books.BOOKS)
                    .set(Books.BOOKS.TITLE, title)
                    .set(Books.BOOKS.PRICE, price)
                    .set(Books.BOOKS.PUBLICATION_STATUS, publicationStatus)
                    .where(Books.BOOKS.ID.eq(id)).execute()

                ctx.deleteFrom(BookAuthors.BOOK_AUTHORS)
                    .where(BookAuthors.BOOK_AUTHORS.BOOK_ID.eq(id))
                    .execute()

                authorIds.forEach { authorId ->
                    ctx.insertInto(BookAuthors.BOOK_AUTHORS)
                        .set(BookAuthors.BOOK_AUTHORS.BOOK_ID, id)
                        .set(BookAuthors.BOOK_AUTHORS.AUTHOR_ID, authorId)
                        .execute()
                }

                ctx.selectFrom(Books.BOOKS)
                    .where(Books.BOOKS.ID.eq(id))
                    .fetchOne()
            }
        } catch (e: Exception) {
            println("Error updating book and authors: ${e.message}")
            null
        }
    }

    fun getBookWithAuthorsById(bookId: Int): BookWithAuthorsResponse? {
        val records = dsl
            .select(
                Books.BOOKS.ID,
                Books.BOOKS.TITLE,
                Books.BOOKS.PRICE,
                Books.BOOKS.PUBLICATION_STATUS,
                Authors.AUTHORS.ID,
                Authors.AUTHORS.NAME,
                Authors.AUTHORS.BIRTH_DATE
            )
            .from(Books.BOOKS)
            .leftJoin(BookAuthors.BOOK_AUTHORS).on(Books.BOOKS.ID.eq(BookAuthors.BOOK_AUTHORS.BOOK_ID))
            .leftJoin(Authors.AUTHORS).on(BookAuthors.BOOK_AUTHORS.AUTHOR_ID.eq(Authors.AUTHORS.ID))
            .where(Books.BOOKS.ID.eq(bookId))
            .fetch()

        // 書籍が見つからない場合はnullを返す
        if (records.isEmpty()) {
            return null
        }

        // 最初のレコードから書籍の基本情報を取得
        val firstRecord = records[0]
        val book = BookWithAuthorsResponse(
            id = firstRecord.get(Books.BOOKS.ID),
            title = firstRecord.get(Books.BOOKS.TITLE),
            price = firstRecord.get(Books.BOOKS.PRICE),
            publicationStatus = firstRecord.get(Books.BOOKS.PUBLICATION_STATUS),
            authors = mutableListOf()
        )

        // 各レコードから著者情報を取得してリストに追加
        records.forEach { record ->
            val authorId = record.get(Authors.AUTHORS.ID)
            if (authorId != null) { // 著者が存在する場合のみ追加
                book.authors.add(
                    AuthorResponse(
                        id = authorId,
                        name = record.get(Authors.AUTHORS.NAME),
                        birthDate = record.get(Authors.AUTHORS.BIRTH_DATE)
                    )
                )
            }
        }

        return book
    }
}