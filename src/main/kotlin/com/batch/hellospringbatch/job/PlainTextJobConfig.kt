package com.batch.hellospringbatch.job

import com.batch.hellospringbatch.core.domain.PlainText
import com.batch.hellospringbatch.core.domain.PlainTextRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.RepositoryItemReader
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import java.util.*

@Configuration
class PlainTextJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val plainTextRepository: PlainTextRepository
) {

    @Bean("plainTextJob")
    fun plainTextJob(plainTextStep: Step): Job {
        return jobBuilderFactory["plainTextJob"]
            .incrementer(RunIdIncrementer())
            .start(plainTextStep)
            .build()
    }

    @JobScope
    @Bean("plainTextStep")
    fun plainTextStep(): Step {
        return stepBuilderFactory["plainTextStep"]
            .chunk<PlainText, String>(5)
            .reader(plainTextReader())
            .processor(plainTextProcessor())
            .writer(plainTextWriter())
            .build()
    }

    @StepScope
    @Bean
    fun plainTextReader(): RepositoryItemReader<PlainText> =
        RepositoryItemReaderBuilder<PlainText>()
            .name("plainTextReader")
            .repository(plainTextRepository)
            .methodName("findBy")
            .pageSize(5) // commitInterval
//            .arguments(listOf(null)) //다른 아규먼트가 있다면
            .sorts(Collections.singletonMap("id", Sort.Direction.DESC))
            .build()

    @StepScope
    @Bean
    fun plainTextProcessor(): ItemProcessor<PlainText, String> =
        ItemProcessor { item -> "processed ${item.text}" }

    @StepScope
    @Bean
    fun plainTextWriter(): ItemWriter<String> =
        ItemWriter { items ->
            items.forEach { println(it) }
            println("==== chunk is finished ====")
        }
}