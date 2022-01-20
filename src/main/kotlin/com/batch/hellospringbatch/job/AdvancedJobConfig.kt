package com.batch.hellospringbatch.job

import com.batch.hellospringbatch.job.validator.LocalDateParameterValidator
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AdvancedJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    val log = KotlinLogging.logger {}

    @Bean
    fun advancedJob(advancedStep: Step): Job =
        jobBuilderFactory["advancedJob"]
            .incrementer(RunIdIncrementer())
            .validator(LocalDateParameterValidator("targetDate"))
            .start(advancedStep)
            .build()

    @JobScope
    @Bean
    fun advancedStep(advancedTasklet: Tasklet) =
        stepBuilderFactory["advancedStep"]
            .tasklet(advancedTasklet)
            .build()

    @StepScope
    @Bean
    fun advancedTasklet(@Value("#{jobParameters['targetDate']}")targetDate: String) =
        Tasklet { contribution, chunkContext ->
            log.info { "[AdvancedJobConfig] JobParameter - targetDate = $targetDate" }
            log.info { "[AdvancedJobConfig] executed advancedTasklet" }
            RepeatStatus.FINISHED
        }
}