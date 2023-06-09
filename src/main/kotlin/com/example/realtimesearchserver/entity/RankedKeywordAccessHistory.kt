package com.example.realtimesearchserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("keyword_ranking_access_history")
data class KeywordRankingAccessHistoryEntity(
    @Id
    val id: Long = 0,
    val keywordRankingId: Long,
    val ipAddress: String
)
