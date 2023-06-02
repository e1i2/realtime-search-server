package com.example.realtimesearchserver.entity

import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("ranked_keyword")
data class RankedKeywordEntity(
    @Id
    val id: Long = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column("`rank`")
    val rank: Int,
    val keyword: String
)

data class RankedKeyword(
    val rank: Int,
    val keyword: String
)
