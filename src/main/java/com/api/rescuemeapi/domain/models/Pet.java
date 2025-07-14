package com.api.rescuemeapi.domain.models;

import com.api.rescuemeapi.domain.constants.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.*;

@Entity
@Table(name = "pet")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class Pet extends Auditable<String> {

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

    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetState state = PetState.AVAILABLE;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetSex sex;

    @Builder.Default
    @Column(nullable = false)
    private String breed = Default.UNSPECIFIED.toString();

    @Builder.Default
    @Column(nullable = false)
    private String color = Default.UNSPECIFIED.toString();

    @Builder.Default
    @Column(nullable = false)
    private Boolean isVaccinated = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isCastrated = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isDewormed = false;

    @Builder.Default
    @Column(nullable = false)
    private String medicalNotes = Default.UNSPECIFIED.toString();

    @Column(nullable = false)
    private UUID referenceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", nullable = false)
    private User ownerUser;

    @Builder.Default
    @OneToMany(mappedBy = "requestedPet", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private Set<Petition> receivedPetitions = new HashSet<>();
}