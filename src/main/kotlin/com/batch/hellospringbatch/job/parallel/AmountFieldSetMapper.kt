package com.batch.hellospringbatch.job.parallel

import com.batch.hellospringbatch.dto.AmountDto
import org.springframework.batch.item.file.mapping.FieldSetMapper
import org.springframework.batch.item.file.transform.FieldSet

class AmountFieldSetMapper: FieldSetMapper<AmountDto> {
    override fun mapFieldSet(fieldSet: FieldSet): AmountDto {
        return AmountDto(
            fieldSet.readInt(0),
            fieldSet.readString(1),
            fieldSet.readInt(2)
        )
    }
}