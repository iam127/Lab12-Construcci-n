package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VetService {

    @Autowired
    private VetRepository vetRepository;

    public List<Vet> findAll() {
        return vetRepository.findAll();
    }

    public Vet findById(Integer id) throws VetNotFoundException {
        Optional<Vet> vet = vetRepository.findById(id);
        if (!vet.isPresent()) {
            throw new VetNotFoundException("Vet not found with id: " + id);
        }
        return vet.get();
    }

    public Vet create(Vet vet) {
        return vetRepository.save(vet);
    }

    public Vet update(Vet vet) throws VetNotFoundException {
        Vet existingVet = findById(vet.getId());
        existingVet.setFirstName(vet.getFirstName());
        existingVet.setLastName(vet.getLastName());
        return vetRepository.save(existingVet);
    }

    public void delete(Integer id) throws VetNotFoundException {
        Vet vet = findById(id);
        vetRepository.delete(vet);
    }
}