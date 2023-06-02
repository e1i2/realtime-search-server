package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import com.example.realtimesearchserver.entity.RankedKeyword
import com.example.realtimesearchserver.entity.RankedKeywordEntity
import com.example.realtimesearchserver.repository.RankedKeywordAccessHistoryRepository
import com.example.realtimesearchserver.repository.RankedKeywordRepository
import io.kotest.core.Tuple2
import io.kotest.core.spec.BeforeTest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import java.time.LocalDateTime
import kotlinx.coroutines.flow.flowOf

class CrawlerServiceTest: DescribeSpec({
    beforeEach {
        clearAllMocks()
    }
}) {
    private val crawlerAdapter = mockk<NateCrawlerAdapter>()
    private val rankedKeywordRepository = mockk<RankedKeywordRepository>()
    private val rankedKeywordAccessHistoryRepository = mockk<RankedKeywordAccessHistoryRepository>()
    private val service = SearchKeywordRankService(crawlerAdapter, rankedKeywordRepository, rankedKeywordAccessHistoryRepository)

    init {
        it("Crawler service 호출 테스트") {
            val createdAt = LocalDateTime.now()
            coEvery { rankedKeywordRepository.findAllByCreatedAtAfter(any()) } returns listOf()
            coEvery { crawlerAdapter.crawl() } returns listOf(RankedKeyword(rank = 1, keyword = "hi"))
            coEvery { rankedKeywordRepository.saveAll(entities = any<List<RankedKeywordEntity>>()) } returns flowOf(RankedKeywordEntity(rank = 1, keyword = "hi", createdAt = createdAt))

            val keywords = service.getRankedKeywords()

            keywords shouldBe listOf(RankedKeywordEntity(rank = 1, keyword = "hi", createdAt = createdAt))
            coVerify(exactly = 1) { crawlerAdapter.crawl() }
        }

        it("Crawler service 호출 테스트 - 이미 크롤링 된 경우") {
            val createdAt = LocalDateTime.now()
            coEvery { rankedKeywordRepository.findAllByCreatedAtAfter(any()) } returns listOf(RankedKeywordEntity(rank = 1, keyword = "hi", createdAt = createdAt))

            val keywords = service.getRankedKeywords()

            keywords shouldBe listOf(RankedKeywordEntity(rank = 1, keyword = "hi", createdAt = createdAt))
            coVerify(exactly = 0) { rankedKeywordRepository.saveAll(any<List<RankedKeywordEntity>>()) }
            coVerify(exactly = 0) { crawlerAdapter.crawl() }
        }
    }
}
