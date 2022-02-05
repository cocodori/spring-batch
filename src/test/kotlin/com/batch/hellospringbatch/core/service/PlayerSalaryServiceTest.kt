package com.batch.hellospringbatch.core.service

import com.batch.hellospringbatch.dto.PlayerDto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockedStatic
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.time.Year

internal class PlayerSalaryServiceTest {
    private lateinit var sut: PlayerSalaryService

    @BeforeEach
    fun setUp() {
        sut = PlayerSalaryService()
    }

    @Test
    fun calcSalaryTest() {
        //given
        val mockYearClass: MockedStatic<Year> = mockStatic(Year::class.java)
        val mockYear = mock(Year::class.java)
        `when`(mockYear.value).thenReturn(2021)
        mockYearClass.`when`<Year> { Year.now() }.thenReturn(mockYear)
        val mockPlayer = mock(PlayerDto::class.java)
        `when`(mockPlayer.birthYear).thenReturn(1980)
        `when`(mockPlayer.id).thenReturn("2")
        `when`(mockPlayer.firstName).thenReturn("first")
        `when`(mockPlayer.lastName).thenReturn("last")
        `when`(mockPlayer.debutYear).thenReturn(2020)
        `when`(mockPlayer.position).thenReturn("position")

        //when
        val actual = sut.calcSalary(mockPlayer)

        //then
        assertEquals(actual.salary, 410000)
    }
}