package com.sgs.ugh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class UghApplication

fun main(args: Array<String>) {
	runApplication<UghApplication>(*args)
}
