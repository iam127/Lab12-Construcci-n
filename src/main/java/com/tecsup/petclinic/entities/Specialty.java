package com.tecsup.petclinic.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "specialties")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "vets")  // ⬅️ CRÍTICO
@ToString(exclude = "vets")           // ⬅️ CRÍTICO
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "specialties", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("specialties")  // ⬅️ Evita serialización circular
    private Set<Vet> vets = new HashSet<>();
}