package jp.quotest.quocdt.response

import java.math.BigDecimal

data class BookWithAuthorsResponse (
    val id: Int,
    val title: String,
    val price: Long,
    val publicationStatus: Boolean,
    val authors: MutableList<AuthorResponse>
)