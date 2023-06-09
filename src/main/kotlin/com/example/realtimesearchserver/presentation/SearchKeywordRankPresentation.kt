package com.example.realtimesearchserver.presentation

import com.example.realtimesearchserver.service.SearchKeywordRankingService
import java.net.URI
import java.time.LocalDateTime
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class SearchKeywordRankPresentation(
    private val searchKeywordRankingService: SearchKeywordRankingService
) {
    @GetMapping("/api-public/realtime-search-keyword/ranking")
    suspend fun getKeywordRankings(): List<KeywordRankingResponse> {
        return searchKeywordRankingService.getKeywordRankings()
            .map { KeywordRankingResponse(it.id, it.ranking, it.keyword, it.createdAt) }
    }

    @GetMapping("/api-public/realtime-search-keyword/redirect/naver")
    suspend fun redirect(
        @RequestParam keywordId: Long,
        @RequestHeader("X-Forwarded-For") ipAddress: String,
        response: ServerHttpResponse
    ) {
        val keyword = searchKeywordRankingService.getKeyword(keywordId, ipAddress)
        response.apply {
            response.statusCode = HttpStatus.PERMANENT_REDIRECT;
            response.headers.location = URI.create("https://search.naver.com/search.naver?ie=utf8&query=$keyword");
        }

        response.setComplete().awaitSingleOrNull()
    }
}

data class KeywordRankingResponse(
    val id: Long,
    val rank: Int,
    val keyword: String,
    val createdAt: LocalDateTime
)
