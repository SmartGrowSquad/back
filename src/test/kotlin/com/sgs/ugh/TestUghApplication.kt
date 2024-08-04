package com.sgs.ugh

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<UghApplication>().with(TestcontainersConfiguration::class).run(*args)
}
