package com.batch.hellospringbatch.job

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HelloJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean("helloJob")
    fun helloJob(helloStep: Step): Job {
        return jobBuilderFactory["helloJob"]
            .incrementer(RunIdIncrementer())
            .start(helloStep)
            .build()
    }

    @JobScope
    @Bean("helloStep")
    fun helloStep(tasklet: Tasklet): Step {
        return stepBuilderFactory["helloStep"]
            .tasklet(tasklet)
            .build()
    }

    @StepScope
    @Bean
    fun tasklet(): Tasklet =
        Tasklet { contribution, chunkContext ->
            println("Hello Spring Batch!!")
            RepeatStatus.FINISHED
        }
}