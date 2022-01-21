package com.batch.hellospringbatch.dto

data class PlayerSalaryDto(
     val id: String,
     val lastName: String,
     val firstName: String,
     val position: String,
     val birthYear: Int,
     val debutYear: Int,
     val salary: Int
) {
    companion object {
        fun of(player: PlayerDto, salary: Int) =
            PlayerSalaryDto(
                player.id,
                player.lastName,
                player.firstName,
                player.position,
                player.birthYear,
                player.debutYear,
                salary
            )
    }
}