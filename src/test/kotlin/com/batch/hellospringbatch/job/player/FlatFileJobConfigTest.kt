package com.batch.hellospringbatch.job.player

import com.batch.hellospringbatch.BatchTestConfig
import com.batch.hellospringbatch.core.service.PlayerSalaryService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.test.AssertFile
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.FileSystemResource
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ContextConfiguration(classes = [FlatFileJobConfig::class, BatchTestConfig::class, PlayerSalaryService::class])
@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBatchTest
@SpringBootTest
internal class FlatFileJobConfigTest {
    @Autowired
    lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Test
    fun success() {
        //when
        val execution: JobExecution = jobLauncherTestUtils.launchJob()

        //then
        assertEquals(execution.exitStatus, ExitStatus.COMPLETED)
        AssertFile.assertFileEquals(
            FileSystemResource("player-salary-list.txt"),
            FileSystemResource("succeed-player-salary-list.txt"))
    }
}