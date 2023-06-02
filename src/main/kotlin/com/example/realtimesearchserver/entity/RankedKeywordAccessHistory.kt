package com.example.realtimesearchserver.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("ranked_keyword_access_hisotry")
data class RankedKeywordAccessHistoryEntity(
    @Id
    val id: Long = 0,
    val rankedKeywordId: Long,
    val ipAddress: String
)
