package com.batch.hellospringbatch.job.parallel

import com.batch.hellospringbatch.dto.AmountDto
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.builder.FlowBuilder
import org.springframework.batch.core.job.flow.Flow
import org.springframework.batch.core.job.flow.support.SimpleFlow
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor

/**
 * 단일 프로세스 멀티 스레드에서 Flow를 이용해 동시에 실행
 */
@Configuration
class ParallelStepJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun parallelJob(splitFlow: Flow): Job {
        return jobBuilderFactory["parallelJob"]
            .incrementer(RunIdIncrementer())
            .start(splitFlow)
            .build()
            .build()
    }

    @Bean
    fun splitFlow(
        taskExecutor: TaskExecutor,
        flowAmountFileStep: Flow,
        flowAnotherStep: Flow
    ): Flow {
        return FlowBuilder<SimpleFlow>("splitFlow")
            .split(taskExecutor)
            .add(flowAmountFileStep, flowAnotherStep)
            .end()
    }

    @Bean
    fun flowAmountFileStep(amountFileStep: Step): Flow {
        return FlowBuilder<SimpleFlow>("flowAmountFileStep")
            .start(amountFileStep)
            .end()
    }

    @Bean
    fun amountFileStep(
        amountFileItemReader: FlatFileItemReader<AmountDto>,
        amountFileItemProcessor: ItemProcessor<AmountDto, AmountDto>,
        amountFileItemWriter: FlatFileItemWriter<AmountDto>,
        taskExecutor: TaskExecutor
    ): Step = stepBuilderFactory["multiThreadStep"]
        .chunk<AmountDto, AmountDto>(5)
        .reader(amountFileItemReader)
        .processor(amountFileItemProcessor)
        .writer(amountFileItemWriter)
        .build()

    @Bean
    fun flowAnotherStep(anotherStep: Step): Flow {
        return FlowBuilder<SimpleFlow>("anotherStep")
            .start(anotherStep)
            .build()
    }

    @Bean
    fun anotherStep(): Step {
        return stepBuilderFactory["anotherStep"]
            .tasklet { contribution, chunkContext ->
                Thread.sleep(500)
                println("Another Step Completed. Thread = ${Thread.currentThread().name}")
                RepeatStatus.FINISHED
            }
            .build()
    }
}