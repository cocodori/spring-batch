package com.batch.hellospringbatch.core.domain

import javax.persistence.*

@Table(name="plain_text")
@Entity
class PlainText(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(nullable = false)
    var text: String
)