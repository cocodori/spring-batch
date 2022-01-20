package com.batch.hellospringbatch.core.domain

import javax.persistence.*

@Table(name="result_text")
@Entity
class ResultText(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false)
    var text: String
)