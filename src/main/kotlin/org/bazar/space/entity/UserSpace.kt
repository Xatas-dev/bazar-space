package org.bazar.space.entity

import jakarta.persistence.*
import org.bazar.space.model.http.UserSpaceRole
import org.hibernate.annotations.ColumnDefault
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "user_space")
class UserSpace (
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "space_id", nullable = false)
    val space: Space,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

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