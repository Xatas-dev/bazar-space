package org.bazar.space.entity

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import java.time.Instant

@Entity
@Table(name = "space")
class Space (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    val id: Long? = null,

    @ColumnDefault("now()")
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @ColumnDefault("now()")
    @Column(name = "updated_at", nullable = false)
    var updatedAt: Instant = Instant.now()
)