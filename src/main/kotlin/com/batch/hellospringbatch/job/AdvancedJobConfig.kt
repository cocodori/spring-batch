package com.batch.hellospringbatch.job

import com.batch.hellospringbatch.job.validator.LocalDateParameterValidator
import mu.KotlinLogging
import org.springframework.batch.core.*
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
import java.lang.RuntimeException

@Configuration
class AdvancedJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    val log = KotlinLogging.logger {}

    @Bean
    fun advancedJob(jobExecutionListener: JobExecutionListener, advancedStep: Step): Job =
        jobBuilderFactory["advancedJob"]
            .incrementer(RunIdIncrementer())
            .validator(LocalDateParameterValidator("targetDate"))
            .listener(jobExecutionListener)
            .start(advancedStep)
            .build()

    @JobScope
    @Bean
    fun jobExecutionListener(): JobExecutionListener =
        object : JobExecutionListener {
            override fun beforeJob(jobExecution: JobExecution) {
                log.info { "[JobExecutionListener#beforeJob] jobExecution is ${jobExecution.status}" }
            }

            override fun afterJob(jobExecution: JobExecution) {
                log.info { "[JobExecutionListener#afterJob] jobExecution is ${jobExecution.status}" }
                if (jobExecution.status == BatchStatus.FAILED) {
                    log.error { "[JobExecutionListener#afterJob] JobExecution is FAILED!!! RECOVER ASAP" }
                    //Notification~
                }
            }
        }


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

            throw RuntimeException()

            log.info { "[AdvancedJobConfig] executed advancedTasklet" }
            RepeatStatus.FINISHED
        }
}