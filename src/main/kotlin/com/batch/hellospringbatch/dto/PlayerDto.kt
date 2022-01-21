package com.batch.hellospringbatch.dto

data class PlayerDto(
    private val id: String,
    private val lastName: String,
    private val firstName: String,
    private val position: String,
    private val birthYear: Int,
    private val debutYear: Int
)