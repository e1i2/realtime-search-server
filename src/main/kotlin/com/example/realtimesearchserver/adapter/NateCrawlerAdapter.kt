package com.example.realtimesearchserver.adapter

import com.example.realtimesearchserver.utils.removeSpecials
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
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
    suspend fun crawl(): List<String> {
        val response = webClient.get()
            .uri {
                it.scheme("https")
                    .host("www.nate.com")
                    .path("/js/data/jsonLiveKeywordDataV1.js")
                    .build()
            }.retrieve()
            .bodyToMono<String>()
            .awaitSingle()

        logger.info { "nate api response: $response" }

        val nateRank = jacksonObjectMapper().readValue<List<List<String>>>(response)
        return nateRank.map { it[1].removeSpecials() }
    }
}