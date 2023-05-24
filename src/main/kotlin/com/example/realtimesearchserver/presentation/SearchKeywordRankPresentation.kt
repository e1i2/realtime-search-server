package com.example.realtimesearchserver.presentation

import com.example.realtimesearchserver.service.SearchKeywordRankService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchKeywordRankPresentation(
    private val searchKeywordRankService: SearchKeywordRankService
) {
    @GetMapping("/api-public/keyword/rank")
    suspend fun getRankedKeywords(): List<KeywordRank> {
        return searchKeywordRankService.getRankedKeywords()
            .mapIndexed { index, keyword -> KeywordRank(index + 1, keyword) }
    }
}

data class KeywordRank(
    val rank: Int,
    val keyword: String
)
