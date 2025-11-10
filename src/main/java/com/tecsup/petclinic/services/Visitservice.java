package com.tecsup.petclinic.services;

import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    public List<Visit> findAll() {
        return visitRepository.findAll();
    }

    public Visit findById(Long id) throws VisitNotFoundException {
        Optional<Visit> visit = visitRepository.findById(id);
        if (!visit.isPresent()) {
            throw new VisitNotFoundException("Visit not found with id: " + id);
        }
        return visit.get();
    }

    public Visit create(Visit visit) {
        return visitRepository.save(visit);
    }

    public Visit update(Visit visit) throws VisitNotFoundException {
        Visit existingVisit = findById(visit.getId());
        existingVisit.setVisitDate(visit.getVisitDate());
        existingVisit.setDescription(visit.getDescription());
        return visitRepository.save(existingVisit);
    }

    public void delete(Long id) throws VisitNotFoundException {
        Visit visit = findById(id);
        visitRepository.delete(visit);
    }
}