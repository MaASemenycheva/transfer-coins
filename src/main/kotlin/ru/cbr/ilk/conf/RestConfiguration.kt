package ru.cbr.ilk.conf

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate


@Configuration
class RestConfiguration {
    @Bean
    fun createRestTemplate(): RestTemplate {
        return RestTemplate()
    }
}
