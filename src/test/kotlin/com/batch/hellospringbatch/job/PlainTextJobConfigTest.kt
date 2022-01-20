package com.batch.hellospringbatch.job

import com.batch.hellospringbatch.BatchTestConfig
import com.batch.hellospringbatch.core.domain.PlainText
import com.batch.hellospringbatch.core.domain.PlainTextRepository
import com.batch.hellospringbatch.core.domain.ResultTextRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.stream.IntStream

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
@ContextConfiguration(classes = [PlainTextJobConfig::class, BatchTestConfig::class])
internal class PlainTextJobConfigTest {
    @Autowired
    lateinit var jobLauncher: JobLauncherTestUtils

    @Autowired
    lateinit var plainTextRepository: PlainTextRepository

    @Autowired
    lateinit var resultTextRepository: ResultTextRepository

    @AfterEach
    fun tearDown() {
        plainTextRepository.deleteAll()
        resultTextRepository.deleteAll()
    }

    @Test
    fun success_givenNoPlainText() {
        //given
        //no PlainText

        //when
        val execution: JobExecution = jobLauncher.launchJob()

        //then
        assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
        assertThat(resultTextRepository.count()).isZero
    }

    @Test
    fun success_givenPlainTexts() {
        //given
        givenPlainTexts(12)

        //when
        val execution: JobExecution = jobLauncher.launchJob()

        //then
        assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
        assertThat(resultTextRepository.count()).isEqualTo(12)
    }

    private fun givenPlainTexts(count: Int) {
        IntStream.range(0, count)
            .forEach { num -> plainTextRepository.save(PlainText(text = "text $num"))}
    }

}