package com.example.realtimesearchserver.adapter

import com.example.realtimesearchserver.entity.KeywordRanking
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import java.net.URI
import java.nio.charset.Charset
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.util.UriBuilder
import reactor.core.publisher.Mono

private const val nateContent = "[[\"1\",  \"野 노란봉투법 본회의\",  \"n\",  \"0\",  \"野 노란봉투법 본회의\"], [\"2\",  \"성추행 의혹 의원\",  \"n\",  \"0\",  \"성추행 의혹 의원\"], [\"3\",  \"나쁜 엄마\",  \"+\",  \"1\",  \"나쁜 엄마\"], [\"4\",  \"김남국 방지법 통과\",  \"n\",  \"0\",  \"김남국 방지법 통과\"], [\"5\",  \"나는 신이다 아가동산\",  \"-\",  \"2\",  \"나는 신이다 아가동산 편\"], [\"6\",  \"누리호 발사 통신\",  \"-\",  \"5\",  \"누리호 발사 통신\"], [\"7\",  \"건축왕 전세사기 피해자\",  \"n\",  \"0\",  \"건축왕 전세사기 피해자\"], [\"8\",  \"골 때리는 그녀들\",  \"n\",  \"0\",  \"골 때리는 그녀들\"], [\"9\",  \"유아인\",  \"-\",  \"3\",  \"유아인\"], [\"10\",  \"박성한 만루포 SSG,\",  \"n\",  \"0\",  \"박성한 만루포 SSG,\"]]"

class CrawlerAdapterTest: DescribeSpec({
    afterEach {
        clearAllMocks()
    }
}) {
    private val response = mockk<WebClient.ResponseSpec>()
    private val spec = mockk<WebClient.RequestHeadersUriSpec<*>>()
    private val client = mockk<WebClient>()

    private val nateCrawlerAdapter = NateCrawlerAdapter(client)

    init {
        describe("네이트 크롤링 어댑터 WebClient 테스트") {
            every { response.bodyToMono<ByteArray>() } returns Mono.just(nateContent.toByteArray(Charset.forName("euc-kr")))
            every { spec.uri(any<java.util.function.Function<UriBuilder, URI>>()) } returns spec
            every { spec.retrieve() } returns response
            every { spec.header("Content-Type", "application/json;charset=UTF-8") } returns spec
            every { client.get() } returns spec

            val result = nateCrawlerAdapter.crawl()

            it("결과 테스트") {
                result shouldBe listOf("野 노란봉투법 본회의", "성추행 의혹 의원", "나쁜 엄마", "김남국 방지법 통과", "나는 신이다 아가동산", "누리호 발사 통신", "건축왕 전세사기 피해자", "골 때리는 그녀들", "유아인", "박성한 만루포 SSG")
                    .mapIndexed { index, keyword -> KeywordRanking(index + 1, keyword) }
            }
        }
    }
}
