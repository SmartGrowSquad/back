package com.sgs.ugh

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UghApplication

fun main(args: Array<String>) {
	runApplication<UghApplication>(*args)
}
