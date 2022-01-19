package com.batch.hellospringbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableBatchProcessing
@SpringBootApplication
class HelloSpringBatchApplication

fun main(args: Array<String>) {
	runApplication<HelloSpringBatchApplication>(*args)
}
