package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SpecialtyService {

    @Autowired
    private SpecialtyRepository specialtyRepository;

    public List<Specialty> findAll() {
        return specialtyRepository.findAll();
    }

    public Specialty findById(Integer id) throws SpecialtyNotFoundException {
        Optional<Specialty> specialty = specialtyRepository.findById(id);
        if (!specialty.isPresent()) {
            throw new SpecialtyNotFoundException("Specialty not found with id: " + id);
        }
        return specialty.get();
    }

    public Specialty create(Specialty specialty) {
        return specialtyRepository.save(specialty);
    }

    public Specialty update(Specialty specialty) throws SpecialtyNotFoundException {
        Specialty existingSpecialty = findById(specialty.getId());
        existingSpecialty.setName(specialty.getName());
        return specialtyRepository.save(existingSpecialty);
    }

    public void delete(Integer id) throws SpecialtyNotFoundException {
        Specialty specialty = findById(id);
        specialtyRepository.delete(specialty);
    }
}