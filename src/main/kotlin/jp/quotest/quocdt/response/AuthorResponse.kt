package jp.quotest.quocdt.response

import java.time.LocalDate

data class AuthorResponse(
    val id: Int,
    val name: String,
    val birthDate: LocalDate?
)