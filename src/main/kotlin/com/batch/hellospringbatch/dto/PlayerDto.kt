package com.batch.hellospringbatch.dto

data class PlayerDto(
     val id: String,
     val lastName: String,
     val firstName: String,
     val position: String,
     val birthYear: Int,
     val debutYear: Int
)