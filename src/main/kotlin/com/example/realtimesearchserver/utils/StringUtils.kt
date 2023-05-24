package com.example.realtimesearchserver.utils

private val specialRegex = """[!@#$%^&*\(\)\[\]\{\};:,./<>?\|`]""".toRegex()

fun String.removeSpecials(): String {
    return this.replace(specialRegex, "")
}