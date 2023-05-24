package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk

class CrawlerServiceTest: DescribeSpec({
    afterEach {
        clearAllMocks()
    }
}) {
    private val crawlerAdapter = mockk<NateCrawlerAdapter>()
    private val service = SearchKeywordRankService(crawlerAdapter)

    init {
        describe("Crawler service 호출 테스트") {
            coEvery { crawlerAdapter.crawl() } returns listOf("hi")

            val keywords = service.getRankedKeywords()

            keywords shouldBe listOf("hi")
            coVerify(exactly = 1) { crawlerAdapter.crawl() }
        }
    }
}
