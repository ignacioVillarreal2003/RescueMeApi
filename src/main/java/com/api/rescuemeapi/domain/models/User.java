package com.api.rescuemeapi.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class User extends Auditable<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Builder.Default
    @OneToMany(mappedBy = "ownerUser", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private Set<Pet> ownedPets = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "requestingUser", cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.LAZY)
    private Set<Petition> sentPetitions =  new HashSet<>();
}
