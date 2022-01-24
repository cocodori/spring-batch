package com.batch.hellospringbatch.job.parallel

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.partition.PartitionHandler
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.core.partition.support.SimplePartitioner
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor

/**
 * 단일 프로세스에서 마스터 스텝과 워크 스텝을 두고,
 * 마스터 스텝에서 생성해준 파티션 단위로 스텝을 병렬 처리한다.
 */
@Configuration
class PartitioningJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    //파티션의 단위
    private val PARTITION_SIZE = 100

    @Bean
    fun partitioningJob(masterStep: Step): Job {
        return jobBuilderFactory["partitioningJob"]
            .incrementer(RunIdIncrementer())
            .start(masterStep)
            .build()
    }

    @JobScope
    @Bean
    fun masterStep(
        partitioner: Partitioner,
        partitionHandler: PartitionHandler
    ): Step {
        return stepBuilderFactory["masterStep"]
            .partitioner("anotherStep", partitioner ) //무엇에 대한 파티셔너인지 이름 지정해줘야 함
            .partitionHandler(partitionHandler)
            .build()
    }

    @StepScope
    @Bean
    fun partitioner(): Partitioner {
        val simplePartitioner = SimplePartitioner()
        simplePartitioner.partition(PARTITION_SIZE)
        return simplePartitioner
    }

    @StepScope
    @Bean
    fun partitionHandler(
        anotherStep: Step,
        taskExecutor: TaskExecutor
    ): TaskExecutorPartitionHandler {
        val partitionHandler = TaskExecutorPartitionHandler()
        //동시 실행할 Step
        partitionHandler.step = anotherStep
        partitionHandler.gridSize = PARTITION_SIZE
        partitionHandler.setTaskExecutor(taskExecutor)

        return partitionHandler
    }
}