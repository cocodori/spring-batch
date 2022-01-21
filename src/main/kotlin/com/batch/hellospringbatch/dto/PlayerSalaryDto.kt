package com.batch.hellospringbatch.dto

data class PlayerSalaryDto(
    private val id: String,
    private val lastName: String,
    private val firstName: String,
    private val position: String,
    private val birthYear: Int,
    private val debutYear: Int,
    private val salary: Int
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