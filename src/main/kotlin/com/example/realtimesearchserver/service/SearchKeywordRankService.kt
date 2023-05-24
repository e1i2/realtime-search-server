package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import org.springframework.stereotype.Service

@Service
class SearchKeywordRankService(
    private val crawlerAdapter: NateCrawlerAdapter
) {
    suspend fun getRankedKeywords(): List<String> {
        return crawlerAdapter.crawl()
    }
}
