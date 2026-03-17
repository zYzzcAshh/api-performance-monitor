package com.apimonitor

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

    @Bean
    fun defaultWebClient(): WebClient = WebClient.create()

    @Bean
    fun testApiWebClient(): WebClient =
        WebClient.builder()
            .baseUrl("https://api.github.com")
            .build()
}
