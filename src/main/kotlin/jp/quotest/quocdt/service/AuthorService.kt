package jp.quotest.quocdt.service

import jp.quotest.quocdt.db.tables.Authors
import jp.quotest.quocdt.db.tables.records.AuthorsRecord
import org.jooq.DSLContext
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class AuthorService(private val dsl: DSLContext) {

    fun getAllAuthors(): List<AuthorsRecord> {
        return dsl.selectFrom(Authors.AUTHORS).fetch()
    }

    fun getAuthorById(id: Int): AuthorsRecord? {
        return dsl.selectFrom(Authors.AUTHORS)
            .where(Authors.AUTHORS.ID.eq(id))
            .fetchOne()
    }

    fun createAuthor(name: String, birthday: LocalDate): AuthorsRecord? {
        return try {
            dsl.insertInto(Authors.AUTHORS)
                .set(Authors.AUTHORS.NAME, name)
                .set(Authors.AUTHORS.BIRTH_DATE, birthday)
                .returning()
                .fetchOne()
        } catch (e: Exception) {
            // Error Log
            null
        }
    }

    fun updateAuthor(id: Int, name: String, birthday: LocalDate): AuthorsRecord? {
        return try {
            dsl.update(Authors.AUTHORS)
                .set(Authors.AUTHORS.NAME, name)
                .set(Authors.AUTHORS.BIRTH_DATE, birthday)
                .where(Authors.AUTHORS.ID.eq(id))
                .returning()
                .fetchOne()
        } catch (e: Exception) {
            // Error Log
            null
        }
    }
}