package com.batch.hellospringbatch.job.player

import com.batch.hellospringbatch.core.service.PlayerSalaryService
import com.batch.hellospringbatch.dto.PlayerDto
import com.batch.hellospringbatch.dto.PlayerSalaryDto
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.adapter.ItemProcessorAdapter
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
import java.io.File

@Configuration
class FlatFileJobConfig(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {
    @Bean
    fun flatFileJob(flatFileStep: Step): Job =
        jobBuilderFactory["flatFileJob"]
            .incrementer(RunIdIncrementer())
            .start(flatFileStep)
            .build()

    @JobScope
    @Bean
    fun flatFileStep(
        playerFileItemReader: FlatFileItemReader<PlayerDto>,
        itemProcessorAdapter: ItemProcessorAdapter<PlayerDto, PlayerSalaryDto>,
        playerFileItemWriter: FlatFileItemWriter<PlayerSalaryDto>
    ): Step =
        stepBuilderFactory["flatFileStep"]
            .chunk<PlayerDto, PlayerSalaryDto>(5)
            .reader(playerFileItemReader)
            .processor(itemProcessorAdapter)
            .writer(playerFileItemWriter)
            .build()

    @StepScope
    @Bean
    fun playerFileItemWriter(): FlatFileItemWriter<PlayerSalaryDto> {
        val fieldExtractor: BeanWrapperFieldExtractor<PlayerSalaryDto> = BeanWrapperFieldExtractor()
        fieldExtractor.setNames(arrayOf("id", "firstName", "lastName", "salary"))
        fieldExtractor.afterPropertiesSet()

        val lineAggregator: DelimitedLineAggregator<PlayerSalaryDto> = DelimitedLineAggregator()
        lineAggregator.setDelimiter("\t")
        lineAggregator.setFieldExtractor(fieldExtractor)

        //기존 파일이 있으면 덮어쓴다.
        File("player-salary-list.txt").createNewFile()
        val resource = FileSystemResource("player-salary-list.txt")

        return FlatFileItemWriterBuilder<PlayerSalaryDto>()
            .name("platFileItemWriter")
            .resource(resource)
            .lineAggregator(lineAggregator)
            .build()
    }

    @StepScope
    @Bean
    fun playerSalaryItemProcessorAdapter(
        playerSalaryService: PlayerSalaryService
    ): ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> {
        val adapter = ItemProcessorAdapter<PlayerDto, PlayerSalaryDto>()
        adapter.setTargetObject(playerSalaryService)
        adapter.setTargetMethod("calcSalary")
        return adapter
    }

    @StepScope
    @Bean
    fun playerSalaryItemProcessor(
        playerSalaryService: PlayerSalaryService
    ): ItemProcessor<PlayerDto, PlayerSalaryDto> =
        ItemProcessor { playerSalaryService.calcSalary(it) }

    @StepScope
    @Bean
    fun playerFileItemReader(): FlatFileItemReader<PlayerDto> =
        FlatFileItemReaderBuilder<PlayerDto>()
            .name("playerFileItemReader")
            .lineTokenizer(DelimitedLineTokenizer())
            .linesToSkip(1)
            .fieldSetMapper(PlayerFieldSetMapper())
            .resource(FileSystemResource("player-list.txt"))
            .build()
}