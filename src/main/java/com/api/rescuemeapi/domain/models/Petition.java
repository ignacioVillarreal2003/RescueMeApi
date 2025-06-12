package com.api.rescuemeapi.domain.models;

import com.api.rescuemeapi.domain.enums.PetitionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "petition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Petition extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetitionStatus status = PetitionStatus.PENDING;

    @Column(nullable = false)
    private String message;

    @ManyToOne()
    @JoinColumn(name = "requested_pet_id", nullable = false)
    private Pet requestedPet;

    @ManyToOne()
    @JoinColumn(name = "requesting_user_id", nullable = false)
    private User requestingUser;
}
