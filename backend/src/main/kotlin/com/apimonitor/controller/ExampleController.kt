package com.apimonitor.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class ExampleController(
    private val defaultWebClient: WebClient,
    private val testApiWebClient: WebClient,
) {
    @GetMapping("/hello")
    fun sayHello(): String = "Hello, World!"

    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<String> = ResponseEntity.ok("Server is alive")

    @GetMapping("/test")
    fun getTestData(): Mono<String> =
        testApiWebClient
            .get()
            .uri("/")
            .retrieve()
            .bodyToMono(String::class.java)
}
