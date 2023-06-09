package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import com.example.realtimesearchserver.entity.KeywordRankingAccessHistoryEntity
import com.example.realtimesearchserver.entity.KeywordRankingEntity
import com.example.realtimesearchserver.repository.KeywordRankingAccessHistoryRepository
import com.example.realtimesearchserver.repository.KeywordRankingRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class SearchKeywordRankingService(
    private val crawlerAdapter: NateCrawlerAdapter,
    private val keywordRankingRepository: KeywordRankingRepository,
    private val keywordRankingAccessHistoryRepository: KeywordRankingAccessHistoryRepository
) {
    suspend fun getKeywordRankings(): List<KeywordRankingEntity> {
        // 동시성 고려 필요
        return keywordRankingRepository.findAllByCreatedAtAfter(LocalDateTime.now().minusMinutes(30))
            .ifEmpty { saveAndGetKeywordRankingEntities() }
    }

    private suspend fun saveAndGetKeywordRankingEntities(): List<KeywordRankingEntity> {
        val keywordRanking = crawlerAdapter.crawl()

        return keywordRankingRepository.saveAll(
            keywordRanking.map { keyword ->
                KeywordRankingEntity(
                    ranking = keyword.ranking,
                    keyword = keyword.keyword,
                    type = "nate"
                )
            }
        ).toList()
    }

    suspend fun getKeyword(keywordId: Long, ipAddress: String): String {
        val keywordRanking = keywordRankingRepository.findById(keywordId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "keyword not found")
        keywordRankingAccessHistoryRepository.save(KeywordRankingAccessHistoryEntity(keywordRankingId = keywordRanking.id, ipAddress = ipAddress))
        return keywordRanking.keyword
    }
}
