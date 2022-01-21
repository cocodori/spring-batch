package com.batch.hellospringbatch.job.player

import com.batch.hellospringbatch.dto.PlayerDto
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class PlayerFieldSetMapper: FieldSetMapper<PlayerDto> {
    override fun mapFieldSet(fieldSet: FieldSet): PlayerDto {
        return PlayerDto(
            fieldSet.readString(0),
            fieldSet.readString(1),
            fieldSet.readString(2),
            fieldSet.readString(3),
            fieldSet.readInt(4),
            fieldSet.readInt(5),
        )
    }
}