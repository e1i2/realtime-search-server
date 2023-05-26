package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import com.example.realtimesearchserver.entity.RankedKeyword
import com.example.realtimesearchserver.entity.RankedKeywordEntity
import com.example.realtimesearchserver.repository.RankedKeywordRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.flow.flowOf

class CrawlerServiceTest: DescribeSpec({
    afterEach {
        clearAllMocks()
    }
}) {
    private val crawlerAdapter = mockk<NateCrawlerAdapter>()
    private val rankedKeywordRepository = mockk<RankedKeywordRepository>()
    private val service = SearchKeywordRankService(crawlerAdapter, rankedKeywordRepository)

    init {
        describe("Crawler service 호출 테스트") {
            val createdAt = LocalDateTime.now()
            coEvery { crawlerAdapter.crawl() } returns listOf(RankedKeyword(rank = 1, keyword = "hi"))
            coEvery { rankedKeywordRepository.saveAll(entities = any<List<RankedKeywordEntity>>()) } returns flowOf(RankedKeywordEntity(rank = 1, keyword = "hi", createdAt = createdAt))

            val keywords = service.getRankedKeywords()

            keywords shouldBe listOf(RankedKeywordEntity(rank = 1, keyword = "hi", createdAt = createdAt))
            coVerify(exactly = 1) { crawlerAdapter.crawl() }
        }
    }
}
