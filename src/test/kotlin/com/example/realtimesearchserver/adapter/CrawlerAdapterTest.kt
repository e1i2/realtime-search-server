package com.example.realtimesearchserver.adapter

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe

class CrawlerAdapterTest: DescribeSpec() {
    init {
        describe("크롤링 어댑터 테스트") {
            CrawlerAdapter() shouldNotBe CrawlerAdapter()
            it("외부 API ")
        }
    }
}
