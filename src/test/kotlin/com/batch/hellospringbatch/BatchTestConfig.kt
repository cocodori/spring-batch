package com.batch.hellospringbatch

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Configuration

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
class BatchTestConfig {
}