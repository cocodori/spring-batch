package com.batch.hellospringbatch.core.service

import com.batch.hellospringbatch.dto.PlayerDto
import com.batch.hellospringbatch.dto.PlayerSalaryDto
import org.springframework.stereotype.Service
import java.time.Year

@Service
class PlayerSalaryService {
    fun calcSalary(player: PlayerDto): PlayerSalaryDto {
        val salary = (Year.now().value - player.birthYear) * 10000
        return PlayerSalaryDto.of(player, salary)
    }
}