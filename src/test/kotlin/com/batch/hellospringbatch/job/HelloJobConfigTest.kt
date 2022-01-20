package com.batch.hellospringbatch.job

import com.batch.hellospringbatch.BatchTestConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBatchTest
@SpringBootTest(classes = [HelloJobConfig::class, BatchTestConfig::class])
@ExtendWith(SpringExtension::class)
@ActiveProfiles("test")
//@ContextConfiguration()
internal class HelloJobConfigTest {
    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Test
    fun success() {
        val execution = jobLauncherTestUtils.launchJob()

        assertThat(execution.exitStatus).isEqualTo(ExitStatus.COMPLETED)
    }
}