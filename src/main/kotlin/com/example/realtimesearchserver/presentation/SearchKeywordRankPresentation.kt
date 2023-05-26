package com.example.realtimesearchserver.presentation

import com.example.realtimesearchserver.service.SearchKeywordRankService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchKeywordRankPresentation(
    private val searchKeywordRankService: SearchKeywordRankService
) {
    @GetMapping("/api-public/keyword/rank")
    suspend fun getRankedKeywords(): List<KeywordRankResponse> {
        return searchKeywordRankService.getRankedKeywords()
            .map { KeywordRankResponse(it.id, it.rank, it.keyword) }
    }

    @GetMapping("/api-public/keyword/redirect/naver")
    suspend fun redirect() {

    }
}

data class KeywordRankResponse(
    val id: Long,
    val rank: Int,
    val keyword: String
)
