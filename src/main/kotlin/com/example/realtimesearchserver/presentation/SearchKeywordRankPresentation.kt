package com.example.realtimesearchserver.presentation

import com.example.realtimesearchserver.service.SearchKeywordRankService
import java.net.URI
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SearchKeywordRankPresentation(
    private val searchKeywordRankService: SearchKeywordRankService
) {
    @GetMapping("/api-public/realtime-search-keyword/rank")
    suspend fun getRankedKeywords(): List<KeywordRankResponse> {
        return searchKeywordRankService.getRankedKeywords()
            .map { KeywordRankResponse(it.id, it.rank, it.keyword) }
    }

    @GetMapping("/api-public/realtime-search-keyword/redirect/naver")
    suspend fun redirect(
        @RequestParam keywordId: Long,
        @RequestHeader("X-Forwarded-For") ipAddress: String,
        response: ServerHttpResponse
    ) {
        val keyword = searchKeywordRankService.getKeyword(keywordId, ipAddress)
        response.apply {
            response.statusCode = HttpStatus.PERMANENT_REDIRECT;
            response.headers.location = URI.create("https://search.naver.com/search.naver?ie=utf8&query=$keyword");
        }

        response.setComplete().awaitSingleOrNull()
    }
}

data class KeywordRankResponse(
    val id: Long,
    val rank: Int,
    val keyword: String
)
