package com.example.realtimesearchserver.utils

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class StringUtilsTest: DescribeSpec() {
    init {
        it("특수문자 제거 util 테스트") {
            val specials = "!@#$%^&*()[]{};:,./<>?|`"
            specials.removeSpecials() shouldBe ""
        }
    }
}
