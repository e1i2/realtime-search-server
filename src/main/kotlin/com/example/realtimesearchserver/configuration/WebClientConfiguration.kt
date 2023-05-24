package com.example.realtimesearchserver.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import io.netty.channel.ChannelOption
import io.netty.channel.socket.nio.NioChannelOption
import jdk.net.ExtendedSocketOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient

@Configuration
class WebClientConfiguration {
    @Bean
    fun webClient(
        webClientBuilder: WebClient.Builder,
        objectMapper: ObjectMapper
    ): WebClient = webClientBuilder
        .clientConnector(
            ReactorClientHttpConnector(
                HttpClient.create()
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(NioChannelOption.of(ExtendedSocketOptions.TCP_KEEPIDLE), 300)
                    .compress(true)
            )
        )
        .exchangeStrategies(
            ExchangeStrategies.withDefaults()
                .mutate()
                .codecs {
                    it.defaultCodecs().run {
                        jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper))
                        jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper))
                    }
                }
                .build()
        )
        .build()
}