package com.batch.hellospringbatch.core.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface ResultTextRepository: JpaRepository<ResultText, Int>