package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PetTypeService {

    @Autowired
    private PetTypeRepository petTypeRepository;

    public List<PetType> findAll() {
        return petTypeRepository.findAll();
    }

    public PetType findById(Integer id) throws PetTypeNotFoundException {
        Optional<PetType> petType = petTypeRepository.findById(id);
        if (!petType.isPresent()) {
            throw new PetTypeNotFoundException("PetType not found with id: " + id);
        }
        return petType.get();
    }

    public PetType create(PetType petType) {
        return petTypeRepository.save(petType);
    }

    public PetType update(PetType petType) throws PetTypeNotFoundException {
        PetType existingPetType = findById(petType.getId());
        existingPetType.setName(petType.getName());
        return petTypeRepository.save(existingPetType);
    }

    public void delete(Integer id) throws PetTypeNotFoundException {
        PetType petType = findById(id);
        petTypeRepository.delete(petType);
    }
}