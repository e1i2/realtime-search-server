package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import com.example.realtimesearchserver.entity.RankedKeywordAccessHistoryEntity
import com.example.realtimesearchserver.entity.RankedKeywordEntity
import com.example.realtimesearchserver.repository.RankedKeywordAccessHistoryRepository
import com.example.realtimesearchserver.repository.RankedKeywordRepository
import java.time.LocalDateTime
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class SearchKeywordRankService(
    private val crawlerAdapter: NateCrawlerAdapter,
    private val rankedKeywordRepository: RankedKeywordRepository,
    private val rankedKeywordAccessHistoryRepository: RankedKeywordAccessHistoryRepository
) {
    suspend fun getRankedKeywords(): List<RankedKeywordEntity> {
        // 동시성 고려 필요
        return rankedKeywordRepository.findAllByCreatedAtAfter(LocalDateTime.now().minusMinutes(30))
            .ifEmpty { saveAndGetRankedKeywordEntities() }
    }

    private suspend fun saveAndGetRankedKeywordEntities(): List<RankedKeywordEntity> {
        val rankedKeywords = crawlerAdapter.crawl()

        return rankedKeywordRepository.saveAll(
            rankedKeywords.map { keyword ->
                RankedKeywordEntity(
                    rank = keyword.rank,
                    keyword = keyword.keyword
                )
            }
        ).toList()
    }

    suspend fun getKeyword(keywordId: Long, ipAddress: String): String {
        val rankedKeyword = rankedKeywordRepository.findById(keywordId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "keyword not found")
        rankedKeywordAccessHistoryRepository.save(RankedKeywordAccessHistoryEntity(rankedKeywordId = rankedKeyword.id, ipAddress = ipAddress))
        return rankedKeyword.keyword
    }
}
