package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import com.example.realtimesearchserver.entity.RankedKeyword
import com.example.realtimesearchserver.entity.RankedKeywordEntity
import com.example.realtimesearchserver.repository.RankedKeywordRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import org.springframework.stereotype.Service

@Service
class SearchKeywordRankService(
    private val crawlerAdapter: NateCrawlerAdapter,
    private val rankedKeywordRepository: RankedKeywordRepository
) {
    suspend fun getRankedKeywords(): List<RankedKeywordEntity> {
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
}
