package com.apimonitor.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api")
class ExampleController(
    private val webClient: WebClient,
) {
    @GetMapping("/hello")
    fun sayHello(): String = "Hello, World!"

    @GetMapping("/health")
    fun healthCheck(): ResponseEntity<String> = ResponseEntity.ok("Server is alive")

    @GetMapping("/github")
    fun getGithubData(): Mono<String> =
        webClient
            .get()
            .uri("https://api.github.com")
            .retrieve()
            .bodyToMono(String::class.java)
}
