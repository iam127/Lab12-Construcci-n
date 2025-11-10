package com.tecsup.petclinic.repositories;

import com.tecsup.petclinic.entities.Vet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Vet entity
 */
@Repository
public interface VetRepository extends JpaRepository<Vet, Integer> {
    // Spring Data JPA provides all basic CRUD operations
}