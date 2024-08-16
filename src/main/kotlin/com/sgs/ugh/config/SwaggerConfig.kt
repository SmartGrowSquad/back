package com.sgs.ugh.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.info.Info
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${springdoc.title}") val title: String
) {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(Components())
        .info(apiInfo())

    private fun apiInfo()= Info()
        .title("$title API DOC")
        .description("For $title service api document")
        .version("1.0.0")
}