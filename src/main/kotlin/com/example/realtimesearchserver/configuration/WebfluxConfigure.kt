package com.example.realtimesearchserver.configuration

import org.springframework.stereotype.Component
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.WebFluxConfigurer

@Component
class WebfluxConfigure : WebFluxConfigurer {
    override fun addCorsMappings(corsRegistry: CorsRegistry) {
        corsRegistry.addMapping("/**")
            .allowedMethods("*")
            .allowedOriginPatterns("*")
            .allowCredentials(true)
            .maxAge(3600)
    }
}