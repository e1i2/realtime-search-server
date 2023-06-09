package com.example.realtimesearchserver.repository

import com.example.realtimesearchserver.entity.KeywordRankingEntity
import java.time.LocalDateTime
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface KeywordRankingRepository: CoroutineCrudRepository<KeywordRankingEntity, Long> {
    suspend fun findAllByCreatedAtAfter(after: LocalDateTime): List<KeywordRankingEntity>
}
