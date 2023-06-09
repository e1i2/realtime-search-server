package com.example.realtimesearchserver.service

import com.example.realtimesearchserver.adapter.NateCrawlerAdapter
import com.example.realtimesearchserver.entity.KeywordRanking
import com.example.realtimesearchserver.entity.KeywordRankingEntity
import com.example.realtimesearchserver.repository.KeywordRankingAccessHistoryRepository
import com.example.realtimesearchserver.repository.KeywordRankingRepository
import io.kotest.core.spec.style.DescribeSpec
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
    private val keywordRankingRepository = mockk<KeywordRankingRepository>()
    private val keywordRankingAccessHistoryRepository = mockk<KeywordRankingAccessHistoryRepository>()
    private val service = SearchKeywordRankingService(crawlerAdapter, keywordRankingRepository, keywordRankingAccessHistoryRepository)

    init {
        it("Crawler service 호출 테스트") {
            val createdAt = LocalDateTime.now()
            coEvery { keywordRankingRepository.findAllByCreatedAtAfter(any()) } returns listOf()
            coEvery { crawlerAdapter.crawl() } returns listOf(KeywordRanking(ranking = 1, keyword = "hi"))
            coEvery { keywordRankingRepository.saveAll(entities = any<List<KeywordRankingEntity>>()) } returns flowOf(KeywordRankingEntity(ranking = 1, keyword = "hi", createdAt = createdAt, type = "nate"))

            val keywords = service.getKeywordRankings()

            keywords shouldBe listOf(KeywordRankingEntity(ranking = 1, keyword = "hi", createdAt = createdAt, type = "nate"))
            coVerify(exactly = 1) { crawlerAdapter.crawl() }
        }

        it("Crawler service 호출 테스트 - 이미 크롤링 된 경우") {
            val createdAt = LocalDateTime.now()
            coEvery { keywordRankingRepository.findAllByCreatedAtAfter(any()) } returns listOf(KeywordRankingEntity(ranking = 1, keyword = "hi", createdAt = createdAt, type = "nate"))

            val keywords = service.getKeywordRankings()

            keywords shouldBe listOf(KeywordRankingEntity(ranking = 1, keyword = "hi", createdAt = createdAt, type = "nate"))
            coVerify(exactly = 0) { keywordRankingRepository.saveAll(any<List<KeywordRankingEntity>>()) }
            coVerify(exactly = 0) { crawlerAdapter.crawl() }
        }
    }
}
