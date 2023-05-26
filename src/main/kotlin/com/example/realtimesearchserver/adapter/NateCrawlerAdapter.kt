package com.example.realtimesearchserver.adapter

import com.example.realtimesearchserver.entity.RankedKeyword
import com.example.realtimesearchserver.utils.removeSpecials
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.charset.Charset
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

private val logger = KotlinLogging.logger {  }

@Component
class NateCrawlerAdapter(
    private val webClient: WebClient
) {
    suspend fun crawl(): List<RankedKeyword> {
        val response = webClient.get()
            .uri {
                it.scheme("https")
                    .host("www.nate.com")
                    .path("/js/data/jsonLiveKeywordDataV1.js")
                    .build()
            }
            .header("Content-Type", "application/json;charset=UTF-8")
            .retrieve()
            .bodyToMono<ByteArray>()
            .awaitSingle()

        val encoded = response.toString(Charset.forName("euc-kr"))
        logger.info { "nate api response: $encoded" }

        val nateRank = jacksonObjectMapper().readValue<List<List<String>>>(encoded)
        return nateRank
            .map { RankedKeyword(it[0].toInt(), it[1].removeSpecials()) }
    }
}