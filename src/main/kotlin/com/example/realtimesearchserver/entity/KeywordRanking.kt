package com.example.realtimesearchserver.entity

import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("keyword_ranking")
data class KeywordRankingEntity(
    @Id
    val id: Long = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val ranking: Int,
    val keyword: String,
    val type: String
)

data class KeywordRanking(
    val ranking: Int,
    val keyword: String
)
