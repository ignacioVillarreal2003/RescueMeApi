package com.api.rescuemeapi.domain.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.List;

@Entity
@Table(name = "usr")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Audited
public class User extends Auditable<Long> {

    @Id
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @OneToMany(mappedBy = "ownerUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> ownedPets;

    @OneToMany(mappedBy = "requestingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Petition> sentPetitions;
}
