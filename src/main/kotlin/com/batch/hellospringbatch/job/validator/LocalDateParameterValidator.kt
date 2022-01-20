package com.batch.hellospringbatch.job.validator

import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersInvalidException
import org.springframework.batch.core.JobParametersValidator
import org.springframework.util.StringUtils
import java.time.LocalDate
import java.time.format.DateTimeParseException

class LocalDateParameterValidator(
    private var parameterName: String
): JobParametersValidator{
    override fun validate(parameters: JobParameters?) {
        val localDate = parameters?.getString(parameterName)

        if (!StringUtils.hasText(localDate))
            throw JobParametersInvalidException("$localDate 가 빈 문자열이거나 존재하지 않습니다")

        try {
            LocalDate.parse(localDate)
        } catch (e: DateTimeParseException) {
            throw JobParametersInvalidException("$localDate 가 날짜 형식이 아닙니다.")
        }
    }
}