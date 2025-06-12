package com.api.rescuemeapi.domain.models;

import com.api.rescuemeapi.domain.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Pet extends Auditable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetSpecies species;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetSize size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetState state = PetState.AVAILABLE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetSex sex;

    @Column(nullable = false)
    private String breed = Default.UNSPECIFIED.toString();

    @Column(nullable = false)
    private String color = Default.UNSPECIFIED.toString();

    @Column(nullable = false)
    private Boolean isVaccinated = Boolean.FALSE;

    @Column(nullable = false)
    private Boolean isCastrated = Boolean.FALSE;

    @Column(nullable = false)
    private Boolean isDewormed = Boolean.FALSE;

    @Column(nullable = false)
    private String medicalNotes = Default.UNSPECIFIED.toString();

    @Column(nullable = false)
    private UUID referenceId;

    @ManyToOne()
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User ownerUser;

    @OneToMany(mappedBy = "requestedPet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Petition> receivedPetitions = new ArrayList<>();
}