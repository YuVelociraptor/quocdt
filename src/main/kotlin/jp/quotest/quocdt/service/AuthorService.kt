package jp.quotest.quocdt.service

import jp.quotest.quocdt.db.tables.Authors
import jp.quotest.quocdt.db.tables.records.AuthorsRecord
import org.jooq.DSLContext
import org.jooq.Result
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Date

@Service
class AuthorService(private val dsl: DSLContext) {

    fun getAllAuthors(): Result<AuthorsRecord> {
        return dsl.selectFrom(Authors.AUTHORS).fetch()
    }

    fun createAuthor(name: String, birthday: LocalDate): Int {
        return dsl.insertInto(Authors.AUTHORS)
            .set(Authors.AUTHORS.NAME, name)
            .set(Authors.AUTHORS.BIRTH_DATE, birthday)
            .execute()
    }
}