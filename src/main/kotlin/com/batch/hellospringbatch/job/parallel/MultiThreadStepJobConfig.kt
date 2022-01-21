package com.batch.hellospringbatch.job.parallel

import com.batch.hellospringbatch.dto.AmountDto
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.file.FlatFileItemReader
import org.springframework.batch.item.file.FlatFileItemWriter
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor
import org.springframework.batch.item.file.transform.DelimitedLineAggregator
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import java.io.File

/**
 * 멀티 프로세스에서 청크 단위로 병렬 처리한다
 */
@Configuration
class MultiThreadStepJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    private val filePath = "data/input.txt"

    @Bean
    fun multiThreadStepJob(multiThreadStep: Step): Job =
        jobBuilderFactory["multiThreadStepJob"]
            .incrementer(RunIdIncrementer())
            .start(multiThreadStep)
            .build()

    @JobScope
    @Bean
    fun multiThreadStep(
        amountFileItemReader: FlatFileItemReader<AmountDto>,
        amountFileItemProcessor: ItemProcessor<AmountDto, AmountDto>,
        amountFileItemWriter: FlatFileItemWriter<AmountDto>,
        taskExecutor: TaskExecutor
    ): Step = stepBuilderFactory["multiThreadStep"]
            .chunk<AmountDto, AmountDto>(5)
            .reader(amountFileItemReader)
            .processor(amountFileItemProcessor)
            .writer(amountFileItemWriter)
            .taskExecutor(taskExecutor)
            .build()

    @Bean
    fun taskExecutor(): TaskExecutor =
        SimpleAsyncTaskExecutor("spring-batch-task-executor")

    @StepScope
    @Bean
    fun amountFileItemReader(): FlatFileItemReader<AmountDto> =
        FlatFileItemReaderBuilder<AmountDto>()
            .name("amountFileItemReader")
            .fieldSetMapper(AmountFieldSetMapper())
            .lineTokenizer(DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB))
            .resource(FileSystemResource(filePath))
            .build()

    @StepScope
    @Bean
    fun amountFileItemProcessor(): ItemProcessor<AmountDto, AmountDto> =
        ItemProcessor {
            println("$it Thread = ${Thread.currentThread().name}")
            it.also { it.amount *= 100 }
        }

    @StepScope
    @Bean
    fun amountFileItemWriter(): FlatFileItemWriter<AmountDto> {
        val fieldExtractor: BeanWrapperFieldExtractor<AmountDto> = BeanWrapperFieldExtractor()
        fieldExtractor.setNames(arrayOf("index", "name", "amount"))

        val lineAggregator = DelimitedLineAggregator<AmountDto>()
        lineAggregator.setFieldExtractor(fieldExtractor)

        val filePath = "data/output.txt"
        File(filePath).createNewFile()

        return FlatFileItemWriterBuilder<AmountDto>()
            .name("amountFileItemWriter")
            .resource(FileSystemResource(filePath))
            .lineAggregator(lineAggregator)
            .build()
    }

}