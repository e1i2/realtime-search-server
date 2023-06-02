package com.example.realtimesearchserver.adapter

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

class WebClientTest: DescribeSpec({
    afterEach {
        clearAllMocks()
    }
}) {
    private val response = mockk<WebClient.ResponseSpec>()
    private val spec = mockk<WebClient.RequestHeadersUriSpec<*>>()
    private val client = mockk<WebClient>()

    init {
        it("WebClient mockking 테스트") {
            val value = "hello"
            every { response.bodyToMono<String>() } returns Mono.just(value)
            every { spec.retrieve() } returns response
            every { client.get() } returns spec

            client.get().retrieve().bodyToMono<String>().awaitSingleOrNull() shouldBe value
        }
    }
}